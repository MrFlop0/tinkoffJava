package edu.java.scrapper;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.core.PostgresDatabase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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

}
