package edu.java.scrapper;

import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.jpa.JpaLinkService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import edu.java.service.jpa.JpaChatService;
import edu.java.service.jpa.JpaLinkUpdater;

@Configuration
@ComponentScan(basePackages = "edu.java.domain")
public class ServiceConfig {
    @Bean
    @Primary
    public JpaLinkService jpaLinkService(JpaLinkRepository jpaLinkRepository, JpaChatRepository jpaChatRepository) {
        return new JpaLinkService(jpaLinkRepository, jpaChatRepository);
    }

    @Bean
    @Primary
    public JpaLinkUpdater jpaLinkUpdater(JpaLinkRepository jpaLinkRepository) {
        return new JpaLinkUpdater(jpaLinkRepository);
    }

    @Bean
    @Primary
    public JpaChatService jpaChatService(JpaChatRepository jpaChatRepository) {
        return new JpaChatService(jpaChatRepository);
    }


}
