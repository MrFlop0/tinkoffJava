package edu.java.service.jooq;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JooqChatService implements ChatService {

    private final JooqChatRepository repository;

    @Override
    public void register(long chatId) {
        repository.add(chatId);
    }

    @Override
    public void unregister(long chatId) {
        repository.delete(chatId);
    }
}
