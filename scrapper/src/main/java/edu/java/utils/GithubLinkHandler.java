package edu.java.utils;

import edu.java.client.github.GitHubClient;
import edu.java.domain.dto.LinkInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("MagicNumber")
@RequiredArgsConstructor
public class GithubLinkHandler {

    private final GitHubClient client;

    public static Pair<String, String> parse(String link) {
        String[] parts = link.split("/");
        if (parts.length < 5 || !parts[0].equals("https:") || !parts[2].equals("github.com")) {
            return null;
        }
        return Pair.of(parts[3], parts[4]);
    }

    public LinkInfo getLinkInfo(String link) {
        var githubParams = parse(link);
        if (githubParams == null) {
            return null;
        }
        var info = client.getRepoInfo(githubParams.getKey(), githubParams.getValue()).block();
        assert info != null;
        return new LinkInfo(link, 1, info.starsCount, null);
    }
}
