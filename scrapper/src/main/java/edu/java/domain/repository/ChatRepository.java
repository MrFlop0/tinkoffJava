package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRepository {
    private final RowMapper<Chat> chatRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public List<Chat> findAll() {
        String request = "select chat_id from chat";
        return jdbcTemplate.query(request, chatRowMapper);
    }

    public boolean add(Long chatId) {
        String request = "insert into chat (chat_id) values (?) on conflict do nothing";
        return jdbcTemplate.update(request, chatId) != 0;
    }

    public boolean delete(Long chatId) {
        String request = "delete from chat where chat_id = ?";
        return jdbcTemplate.update(request, chatId) != 0;
    }

}
