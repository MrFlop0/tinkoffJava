package edu.java.client.bot;

import edu.java.client.bot.dto.Request.LinkUpdateRequest;
import edu.java.client.bot.dto.Response.ApiErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BotClient {
    private final WebClient botClient;

    public Mono<Void> update(LinkUpdateRequest request) {
        return botClient.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                this::handleException
            )
            .bodyToMono(Void.class);
    }

    private Mono<RuntimeException> handleException(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(ApiErrorResponse.class)
            .flatMap(it -> Mono.error(new RuntimeException(it.exceptionMessage())));
    }
}
