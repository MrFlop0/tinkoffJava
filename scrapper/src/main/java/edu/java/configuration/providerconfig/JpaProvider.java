package edu.java.configuration.providerconfig;

import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkService;
import edu.java.service.jpa.JpaLinkUpdater;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database", havingValue = "jpa")
public class JpaProvider {

    @Bean
    public LinkService linkService(
        JpaChatRepository chatRepository,
        JpaLinkRepository linkRepository
    ) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JpaLinkRepository linkRepository) {
        return new JpaLinkUpdater(linkRepository);
    }

    @Bean
    public ChatService chatService(
        JpaChatRepository chatRepository
    ) {
        return new JpaChatService(chatRepository);
    }

}
