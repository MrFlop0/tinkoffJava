package edu.java.domain.repository;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import java.util.List;

public interface LinkToChatRepository {

    List<LinkChat> findAll();

    boolean add(String link, Long chatId);

    boolean delete(String link, Long chatId);

    List<Chat> findChatsByLink(String link);

    List<Link> findLinksByChat(Long chatId);

}

