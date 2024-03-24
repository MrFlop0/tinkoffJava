package edu.java.service.jpa;

import edu.java.domain.entity.ChatEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaChatService implements ChatService {

    private final JpaChatRepository repository;

    @Override
    public void register(long chatId) {
        if (repository.existsById(chatId)) {
            return;
        }
        var chat = new ChatEntity();
        chat.setChatId(chatId);
        repository.save(chat);
    }

    @Override
    public void unregister(long chatId) {
        if (repository.existsById(chatId)) {
            repository.deleteById(chatId);
        }
    }
}
