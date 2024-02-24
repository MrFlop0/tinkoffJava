package edu.java.client;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

public abstract class BaseClient {

    protected abstract WebClient getWebClient();

    public <T> Mono<T> processGetQuery(String uri, Class<T> clazz) {
        return getWebClient().get()
            .uri(uri)
            .retrieve()
            .onStatus(
                httpStatusCode -> httpStatusCode.is4xxClientError() || httpStatusCode.is5xxServerError(),
                clientResponse -> Mono.error(new WebClientResponseException(
                        clientResponse.statusCode().value(),
                        "Server Error occured",
                        null, null, null
                    )
                )
            )
            .bodyToMono(clazz);
    }

}
