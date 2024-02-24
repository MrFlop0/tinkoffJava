package edu.java.client.github;

import edu.java.client.BaseClient;
import edu.java.client.github.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubClient extends BaseClient {
    private final WebClient githubWebClient;

    public Mono<UserInfo> getUserInfo(String username) {
        return processGetQuery("/users/" + username, UserInfo.class);
    }

    @Override
    protected WebClient getWebClient() {
        return githubWebClient;
    }
}
