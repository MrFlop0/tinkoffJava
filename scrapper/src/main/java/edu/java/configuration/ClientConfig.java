package edu.java.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClient githubWebClient(ApplicationConfig cfg) {
        return WebClient.builder()
            .baseUrl(cfg.urls().github())
            .build();
    }

    @Bean
    public WebClient stackOverflowWebClient(ApplicationConfig cfg) {
        return WebClient.builder()
            .baseUrl(cfg.urls().stackoverflow())
            .build();
    }

    @Bean
    public WebClient botClient(ApplicationConfig cfg) {
        return WebClient.builder()
            .baseUrl(cfg.urls().stackoverflow())
            .build();
    }
}
