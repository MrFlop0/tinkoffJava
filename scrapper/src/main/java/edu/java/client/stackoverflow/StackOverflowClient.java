package edu.java.client.stackoverflow;

import edu.java.client.stackoverflow.dto.QuestionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StackOverflowClient {
    private final WebClient stackOverflowWebClient;

    public Mono<QuestionInfo> getQuestionInfo(Long id) {
        return stackOverflowWebClient.get()
            .uri("/2.3/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
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
            .bodyToMono(QuestionInfo.class);
    }
}
