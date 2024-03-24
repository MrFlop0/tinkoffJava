package edu.java.configuration.providerconfig;

import edu.java.domain.repository.jooq.JooqChatRepository;
import edu.java.domain.repository.jooq.JooqLinkRepository;
import edu.java.domain.repository.jooq.JooqLinkToChatRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jooq.JooqChatService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database", havingValue = "jooq")
public class JooqProvider {

    @Bean
    public LinkService linkService(
        JooqLinkToChatRepository listLinksRepository,
        JooqLinkRepository linkRepository
    ) {
        return new JooqLinkService(linkRepository, listLinksRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JooqLinkRepository linkRepository
    ) {
        return new JooqLinkUpdater(linkRepository);
    }

    @Bean
    public ChatService chatService(
        JooqChatRepository chatRepository
    ) {
        return new JooqChatService(chatRepository);
    }

}
