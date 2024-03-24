package edu.java.service.jpa;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.entity.ChatEntity;
import edu.java.domain.entity.LinkEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkService implements LinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Override
    public boolean add(long chatId, LinkInfo info) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            chat = new ChatEntity();
            chat.setChatId(chatId);
            chatRepository.save(chat);
        }

        var link = linkRepository.findByLink(info.url());

        if (link == null) {
            link = new LinkEntity();
            link.setLink(info.url());
            link.setPreviousCheck(OffsetDateTime.now());
            linkRepository.save(link);
        }

        chat.getLinks().add(link);
        link.getChats().add(chat);
        chatRepository.save(chat);
        linkRepository.save(link);

        return true;
    }

    @Override
    public boolean delete(long chatId, String url) {
        ChatEntity chat = chatRepository.findById(chatId).orElse(null);
        var link = linkRepository.findByLink(url);

        if (chat == null || link == null) {
            return false;
        }

        chat.getLinks().remove(link);
        link.getChats().remove(chat);
        chatRepository.save(chat);
        linkRepository.save(link);

        if (link.getChats().isEmpty()) {
            linkRepository.delete(link);
        }
        return true;
    }

    @Override
    public List<Link> findLinksByChat(long chatId) {
        var chat = chatRepository.findById(chatId).orElse(null);
        if (chat == null) {
            return List.of();
        }
        return chat.getLinks().stream().map(LinkEntity::toDto).toList();
    }

    @Override
    public List<Chat> findChatsByLink(String url) {
        var link = linkRepository.findByLink(url);
        if (link == null) {
            return List.of();
        }
        return link.getChats().stream().map(ChatEntity::toDto).toList();
    }

    @Override
    public List<Link> findLinksToCheck() {
        return linkRepository.findLinksToCheck().stream().map(LinkEntity::toDto).toList();
    }
}
