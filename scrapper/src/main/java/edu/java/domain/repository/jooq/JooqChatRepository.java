package edu.java.domain.repository.jooq;

import edu.java.domain.dto.Chat;
import edu.java.domain.repository.ChatRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.tables.Chat.CHAT;

@Repository
@RequiredArgsConstructor
public class JooqChatRepository implements ChatRepository {

    private final DSLContext dslContext;

    @Override
    public List<Chat> findAll() {
        return dslContext.selectFrom(CHAT)
            .fetchInto(Chat.class);
    }

    @Override
    public boolean add(Long chatId) {
        return dslContext.insertInto(CHAT)
            .set(CHAT.CHAT_ID, chatId)
            .execute() != 0;
    }

    @Override
    public boolean delete(Long chatId) {
        return dslContext.deleteFrom(CHAT)
            .where(CHAT.CHAT_ID.eq(chatId))
            .execute() != 0;
    }
}
