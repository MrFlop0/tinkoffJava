package edu.java.configuration;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.sql.DataSource;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

@Configuration
public class DBConfig {

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
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url(URL)
            .username(POSTGRES)
            .password(POSTGRES)
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
