package edu.java.service.jdbc;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final JdbcChatRepository repository;

    @Override
    public void register(long chatId) {
        repository.add(chatId);
    }

    @Override
    public void unregister(long chatId) {
        repository.delete(chatId);
    }
}
