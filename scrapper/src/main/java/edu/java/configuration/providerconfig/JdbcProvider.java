package edu.java.configuration.providerconfig;

import edu.java.domain.repository.jdbc.JdbcChatRepository;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.domain.repository.jdbc.JdbcLinkToChatRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jdbc.JdbcChatService;
import edu.java.service.jdbc.JdbcLinkService;
import edu.java.service.jdbc.JdbcLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database", havingValue = "jdbc")
public class JdbcProvider {

    @Bean
    public LinkService linkService(
        JdbcLinkToChatRepository listLinksRepository,
        JdbcLinkRepository linkRepository
    ) {
        return new JdbcLinkService(linkRepository, listLinksRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(
        JdbcLinkRepository linkRepository
    ) {
        return new JdbcLinkUpdater(linkRepository);
    }

    @Bean
    public ChatService chatService(
        JdbcChatRepository chatRepository
    ) {
        return new JdbcChatService(chatRepository);
    }

}
