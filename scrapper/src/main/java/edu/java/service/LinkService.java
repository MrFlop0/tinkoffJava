package edu.java.service;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import java.util.List;

public interface LinkService {

    boolean add(long chatId, String url, int type);
    boolean delete(long chatId, String url);

    List<Link> findLinksByChat(long chatId);

    List<Chat> findChatsByLink(String url);

    List<Link> findLinksToCheck();

}
