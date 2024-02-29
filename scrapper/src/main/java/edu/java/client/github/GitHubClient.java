package edu.java.client.github;

import edu.java.client.BaseClient;
import edu.java.client.github.dto.RepoInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GitHubClient extends BaseClient {
    private final WebClient githubWebClient;

    public Mono<RepoInfo> getRepoInfo(String owner, String repo) {
        String uri = String.format("/repos/%s/%s", owner, repo);
        return processGetQuery(uri, RepoInfo.class);
    }

    @Override
    protected WebClient getWebClient() {
        return githubWebClient;
    }
}
