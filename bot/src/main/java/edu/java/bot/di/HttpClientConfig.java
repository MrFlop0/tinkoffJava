package edu.java.bot.di;

import edu.java.bot.configuration.ApplicationConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HttpClientConfig {

    @Bean
    public WebClient scrapperWebClient(ApplicationConfig cfg) {
        return WebClient.builder()
            .baseUrl(cfg.baseUrls().scrapper())
            .build();
    }

}
