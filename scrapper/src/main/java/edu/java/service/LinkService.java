package edu.java.service;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import java.util.List;

public interface LinkService {

    boolean add(long chatId, LinkInfo info);

    boolean delete(long chatId, String url);

    List<Link> findLinksByChat(long chatId);

    List<Chat> findChatsByLink(String url);

    List<Link> findLinksToCheck();

}
