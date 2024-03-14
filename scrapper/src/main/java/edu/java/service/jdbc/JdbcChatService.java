package edu.java.service.jdbc;

import edu.java.domain.repository.ChatRepository;
import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcChatService implements ChatService {

    private final ChatRepository repository;
    @Override
    public void register(long chatId) {
        repository.add(chatId);
    }
}
