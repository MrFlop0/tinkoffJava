package edu.java.client.github;

import edu.java.client.github.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubClient {
    private WebClient githubClient;

    public Mono<UserInfo> getUserInfo(String username) {
        return githubClient.get()
            .uri("/users/{username}", username)
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
            .bodyToMono(UserInfo.class);
    }

}
