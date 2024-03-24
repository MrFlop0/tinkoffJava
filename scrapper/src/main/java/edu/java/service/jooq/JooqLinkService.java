package edu.java.service.jooq;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqLinkToChatRepository;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class JooqLinkService implements LinkService {

    private final JooqLinkRepository repository;
    private final JooqLinkToChatRepository mapperRepository;

    @Override
    public boolean add(long chatId, LinkInfo info) {
        return repository.add(info) && mapperRepository.add(info.url(), chatId);
    }

    @Override
    public boolean delete(long chatId, String url) {
        return mapperRepository.delete(url, chatId) && repository.delete(url);
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        return mapperRepository.findLinksByChat(chatId);
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        return mapperRepository.findChatsByLink(url);
    }

    @Override
    public List<Link> findLinksToCheck() {
        return repository.findLinksToCheck();
    }
}
