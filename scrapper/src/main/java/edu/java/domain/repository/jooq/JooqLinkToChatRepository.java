package edu.java.domain.repository.jooq;

import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.repository.LinkToChatRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;
import static edu.java.domain.jooq.Tables.LINKS_CHATS;

@Repository
@RequiredArgsConstructor
public class JooqLinkToChatRepository implements LinkToChatRepository {

    private final DSLContext dslContext;

    @Override
    public List<LinkChat> findAll() {
        return dslContext.selectFrom(LINKS_CHATS.join(LINK).using(LINK.LINK_))
            .fetchInto(LinkChat.class);
    }

    @Override
    public boolean add(String link, Long chatId) {
        return dslContext.insertInto(LINKS_CHATS)
            .set(LINKS_CHATS.LINK, link)
            .set(LINKS_CHATS.CHAT_ID, chatId)
            .onConflictDoNothing()
            .execute() != 0;
    }

    @Override
    public boolean delete(String link, Long chatId) {
        return dslContext.deleteFrom(LINKS_CHATS)
            .where(LINKS_CHATS.LINK.eq(link))
            .and(LINKS_CHATS.CHAT_ID.eq(chatId))
            .execute() != 0;
    }

    @Override
    public List<Chat> findChatsByLink(String link) {
        return dslContext.selectFrom(LINKS_CHATS.join(LINK).using(LINK.LINK_))
            .where(LINKS_CHATS.LINK.eq(link))
            .fetchInto(LinkChat.class)
            .stream()
            .map(it -> new Chat(it.chatId()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Link> findLinksByChat(Long chatId) {
        return dslContext.selectFrom(LINKS_CHATS.join(LINK).using(LINK.LINK_))
            .where(LINKS_CHATS.CHAT_ID.eq(chatId))
            .fetchInto(LinkChat.class)
            .stream()
            .map(it -> new Link(
                it.link(),
                it.type(),
                it.starsCount(),
                it.answerCount(),
                it.updateDate(),
                it.previousCheck()
            ))
            .collect(Collectors.toList());
    }
}
