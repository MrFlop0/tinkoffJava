package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import java.util.List;

public interface ChatRepository {

    List<Chat> findAll();

    boolean add(Long chatId);

    boolean delete(Long chatId);
}
