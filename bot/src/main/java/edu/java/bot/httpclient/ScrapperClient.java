package edu.java.bot.httpclient;

import edu.java.bot.httpclient.dto.Request.AddLinkRequest;
import edu.java.bot.httpclient.dto.Request.RemoveLinkRequest;
import edu.java.bot.httpclient.dto.Response.ApiErrorResponse;
import edu.java.bot.httpclient.dto.Response.LinkResponse;
import edu.java.bot.httpclient.dto.Response.ListLinksResponse;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ScrapperClient {

    private final static String TG_HEADER = "Tg-Chat-Id";
    private final static String LINKS_URI = "/links";
    private final static String TG_URI = "/tg-chat/{id}";

    private final WebClient scrapperClient;

    public Mono<Void> register(Long id) {
        return genericTelegramOperation(HttpMethod.POST, id);
    }

    public Mono<Void> unregister(Long id) {
        return genericTelegramOperation(HttpMethod.DELETE, id);
    }

    public Mono<ListLinksResponse> getLinks(Long chatId) {
        return scrapperClient.get()
            .uri(LINKS_URI)
            .header(TG_HEADER, chatId.toString())
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                this::handleException
            )
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> addLink(Long chatId, String request) {
        return genericLinkOperation(
            chatId,
            new AddLinkRequest(URI.create(request)),
            HttpMethod.POST
        );
    }

    public Mono<LinkResponse> removeLink(Long chatId, String request) {
        return genericLinkOperation(
            chatId,
            new RemoveLinkRequest(URI.create(request)),
            HttpMethod.DELETE
        );
    }

    private Mono<LinkResponse> genericLinkOperation(Long chatId, Object body, HttpMethod method) {
        return scrapperClient.method(method)
            .uri(LINKS_URI)
            .header(TG_HEADER, chatId.toString())
            .bodyValue(body)
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                this::handleException
            )
            .bodyToMono(LinkResponse.class);
    }

    private Mono<Void> genericTelegramOperation(HttpMethod method, Long id) {
        return scrapperClient.method(method)
            .uri(TG_URI, id)
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
