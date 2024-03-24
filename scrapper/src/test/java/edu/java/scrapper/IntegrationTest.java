package edu.java.scrapper;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderNameCase;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class IntegrationTest {
    public static PostgreSQLContainer<?> POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("scrapper")
            .withUsername("postgres")
            .withPassword("postgres");
        POSTGRES.start();

        runMigrations(POSTGRES);
    }

    private static void runMigrations(JdbcDatabaseContainer<?> c) {
        Path migrationPath = new File(".").toPath().toAbsolutePath().getParent().getParent().resolve("migrations");
        try {
            Connection connection =
                DriverManager.getConnection(
                    c.getJdbcUrl(),
                    c.getUsername(),
                    c.getPassword()
                );
            ResourceAccessor changelogDirectory = new DirectoryResourceAccessor(migrationPath);
            PostgresDatabase db = new PostgresDatabase();
            db.setConnection(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("master.yaml", changelogDirectory, db);
            liquibase.update("");

            liquibase.close();
            changelogDirectory.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DynamicPropertySource
    static void jdbcProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @TestConfiguration
    public static class ManagerConfig {

        @Primary
        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .driverClassName(POSTGRES.getDriverClassName())
                .url(POSTGRES.getJdbcUrl())
                .password(POSTGRES.getPassword())
                .username(POSTGRES.getUsername())
                .build();
        }

        @Bean
        public PlatformTransactionManager manager(DataSource source) {
            return new JdbcTransactionManager(source);
        }
    }

    @TestConfiguration
    public static class JpaConfig {
        private static final String URL = "jdbc:postgresql://localhost:5432/scrapper";
        private static final String POSTGRES = "postgres";
        private static final String CHAT_ID = "chat_id";
        private static final String LINK = "link";
        private static final String TYPE = "type";
        private static final String UPDATE_DATE = "update_date";
        private static final String PREVIOUS_CHECK = "previous_check";

        private static final String STARS_COUNT = "stars_count";
        private static final String ANSWER_COUNT = "answers_count";

        @Bean
        public DataSource testDataSource() {
            return DataSourceBuilder.create()
                .driverClassName(IntegrationTest.POSTGRES.getDriverClassName())
                .url(IntegrationTest.POSTGRES.getJdbcUrl())
                .password(IntegrationTest.POSTGRES.getPassword())
                .username(IntegrationTest.POSTGRES.getUsername())
                .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource source) {
            return new JdbcTemplate(source);
        }

        @Bean
        public RowMapper<Chat> chatRowMapper() {
            return (resultSet, rowNum) ->
                new Chat(resultSet.getLong(CHAT_ID));
        }

        @Bean
        public RowMapper<Link> linkRowMapper() {
            return (resultSet, rowNum) ->
                new Link(
                    resultSet.getString(LINK),
                    resultSet.getInt(TYPE),
                    resultSet.getLong(STARS_COUNT),
                    resultSet.getLong(ANSWER_COUNT),
                    timestampToOffsetDate(resultSet.getTimestamp(UPDATE_DATE)),
                    timestampToOffsetDate(resultSet.getTimestamp(PREVIOUS_CHECK))
                );

        }

        @Bean
        public RowMapper<LinkChat> linkChatRowMapper() {
            return (resultSet, rowNum) ->
                new LinkChat(
                    resultSet.getLong(CHAT_ID),
                    resultSet.getString(LINK),
                    resultSet.getInt(TYPE),
                    resultSet.getLong(STARS_COUNT),
                    resultSet.getLong(ANSWER_COUNT),
                    timestampToOffsetDate(resultSet.getTimestamp(UPDATE_DATE)),
                    timestampToOffsetDate(resultSet.getTimestamp(PREVIOUS_CHECK))

                );
        }

        @Bean
        public DataSourceConnectionProvider dataSourceConnectionProvider(DataSource dataSource) {
            return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
        }

        @Bean
        public DSLContext dslContext(DataSourceConnectionProvider connectionProvider) {
            return new DefaultDSLContext(new DefaultConfiguration()
                .set(connectionProvider)
                .set(SQLDialect.POSTGRES)
                .set(new DefaultExecuteListenerProvider(new JooqExceptionTranslator()))
                .set(new Settings().withRenderQuotedNames(RenderQuotedNames.NEVER).withRenderNameCase(RenderNameCase.AS_IS))
            );
        }

        private OffsetDateTime timestampToOffsetDate(Timestamp timestamp) {
            return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
        }
    }


}
