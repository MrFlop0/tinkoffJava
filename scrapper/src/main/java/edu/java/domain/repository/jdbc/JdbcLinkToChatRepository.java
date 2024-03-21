package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.LinkToChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkToChatRepository implements LinkToChatRepository {

    private final RowMapper<LinkChat> linkChatRowMapper;
    private final RowMapper<Chat> chatRowMapper;
    private final RowMapper<Link> linkRowMapper;

    private final JdbcTemplate jdbcTemplate;

    public List<LinkChat> findAll() {
        String request = "select * from links_chats join link using (link)";
        return jdbcTemplate.query(request, linkChatRowMapper);
    }

    public boolean add(String link, Long chatId) {
        String request = "insert into links_chats (chat_id, link) values (?, ?) on conflict do nothing";
        return jdbcTemplate.update(request, chatId, link) != 0;
    }

    public boolean delete(String link, Long chatId) {
        String request = "delete from links_chats where chat_id = ? and link = ?";
        return jdbcTemplate.update(request, chatId, link) != 0;
    }

    public List<Chat> findChatsByLink(String link) {
        String request = "select * from links_chats join link using (link) where link = ?";
        return jdbcTemplate.query(request, chatRowMapper, link);
    }

    public List<Link> findLinksByChat(Long chatId) {
        String request = "select * from links_chats join link using (link) where chat_id = ?";
        return jdbcTemplate.query(request, linkRowMapper, chatId);
    }
}
