package edu.java.configuration;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Configuration
public class DBConfig {

    private static final String URL = "jdbc:postgresql://localhost:5432/scrapper";
    private static final String POSTGRES = "postgres";
    private static final String CHAT_ID = "chat_id";
    private static final String LINK = "link";
    private static final String TYPE = "type";
    private static final String UPDATE_DATE = "update_date";
    private static final String PREVIOUS_CHECK = "previous_check";

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
                timestampToOffsetDate(resultSet.getTimestamp(UPDATE_DATE)),
                timestampToOffsetDate(resultSet.getTimestamp(PREVIOUS_CHECK))
            );

    }

    @Bean
    public RowMapper<LinkChat> linkChatRowMapper() {
        return (resultSet, rowNum) ->
            new LinkChat(
                new Chat(resultSet.getLong(CHAT_ID)),
                new Link(
                    resultSet.getString(LINK),
                    resultSet.getInt(TYPE),
                    timestampToOffsetDate(resultSet.getTimestamp(UPDATE_DATE)),
                    timestampToOffsetDate(resultSet.getTimestamp(PREVIOUS_CHECK))
                )
            );
    }

    private OffsetDateTime timestampToOffsetDate(Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }

}
