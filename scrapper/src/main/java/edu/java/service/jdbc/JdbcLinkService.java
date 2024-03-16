package edu.java.service.jdbc;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {

    private final LinkRepository linkRepository;
    private final LinkToChatRepository mapperRepository;

    @Override
    public boolean add(long chatId, LinkInfo info) {
        return linkRepository.add(info) && mapperRepository.add(info.url(), chatId);
    }

    @Override
    public boolean delete(long chatId, String url) {
        return mapperRepository.delete(url, chatId) && linkRepository.delete(url);
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
        return linkRepository.findLinksToCheck();
    }
}
