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

    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
            .driverClassName("org.postgresql.Driver")
            .url("jdbc:postgresql://localhost:5432/scrapper")
            .username("postgres")
            .password("postgres")
            .build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource source) {
        return new JdbcTemplate(source);
    }

    @Bean
    public RowMapper<Chat> chatRowMapper() {
        return (resultSet, rowNum) ->
            new Chat(resultSet.getLong("chat_id"));
    }

    @Bean
    public RowMapper<Link> linkRowMapper() {
        return (resultSet, rowNum) ->
            new Link(
                resultSet.getString("link"),
                resultSet.getInt("type"),
                timestampToOffsetDate(resultSet.getTimestamp("update_date")),
                timestampToOffsetDate(resultSet.getTimestamp("previous_check"))
            );

    }

    @Bean
    public RowMapper<LinkChat> linkChatRowMapper() {
        return (resultSet, rowNum) ->
            new LinkChat(
                new Chat(resultSet.getLong("chat_id")),
                new Link(
                    resultSet.getString("link"),
                    resultSet.getInt("type"),
                    timestampToOffsetDate(resultSet.getTimestamp("update_date")),
                    timestampToOffsetDate(resultSet.getTimestamp("previous_check"))
                )
            );
    }

    private OffsetDateTime timestampToOffsetDate(Timestamp timestamp) {
        return OffsetDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.of("Z"));
    }

}
