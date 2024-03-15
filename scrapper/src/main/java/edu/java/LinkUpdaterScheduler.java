package edu.java;

import edu.java.client.bot.BotClient;
import edu.java.client.bot.dto.Request.LinkUpdateRequest;
import edu.java.client.github.GitHubClient;
import edu.java.client.github.dto.RepoInfo;
import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.client.stackoverflow.dto.QuestionInfo;
import edu.java.domain.dto.Chat;
import edu.java.service.LinkService;
import edu.java.service.LinkUpdater;
import edu.java.utils.GithubLinkParser;
import edu.java.utils.StackoverflowLinkParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final LinkUpdater linkUpdater;
    private final LinkService linkService;
    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;

    @Scheduled(fixedDelayString = "#{@'app-edu.java.configuration.ApplicationConfig'.scheduler.interval}")
    public void update() {
        log.debug("i'm updating ...");
        linkUpdater.getLinksToCheck().forEach(link -> {
            switch (link.type()) {
                case 1 -> {
                    Pair<String, String> info = GithubLinkParser.parse(link.link());
                    if (info != null) {
                        gitHubClient.getRepoInfo(info.getKey(), info.getValue()).subscribe(
                            githubOnSuccess(link.link()),
                            onError(link.link())
                        );
                    }
                }
                case 0 -> {
                    Long id = StackoverflowLinkParser.parse(link.link());
                    if (id != null) {
                        stackOverflowClient.getQuestionInfo(id).subscribe(
                            stackoverflowOnSuccess(link.link()),
                            onError(link.link())
                        );
                    }
                }
                default -> log.error("unsupported link type {}", link.type());
            }
            linkUpdater.updateCheckDate(link.link());
        });

    }

    private Consumer<Throwable> onError(String link) {
        return (error) -> log.error("couldn't update link = {}\n {}", link, error.getMessage());
    }

    private Consumer<RepoInfo> githubOnSuccess(String link) {
        return (info) -> {
            log.debug("github link updated {} {}", link, info.updateDate);
            if (linkUpdater.refreshUpdateDate(link, info.updateDate)) {
                try {
                    var request = new LinkUpdateRequest(
                        0L,
                        new URI(link),
                        "updated github link",
                        linkService.findChatsByLink(link).stream().map(Chat::chatId).collect(Collectors.toList())
                    );
                    botClient.update(request).subscribe();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private Consumer<QuestionInfo> stackoverflowOnSuccess(String link) {
        return (info) -> {
            if (linkUpdater.refreshUpdateDate(link, info.items.getFirst().lastActivityDate())) {
                try {
                    var request = new LinkUpdateRequest(
                        0L,
                        new URI(link),
                        "updated stackoverflow link",
                        linkService.findChatsByLink(link).stream().map(Chat::chatId).collect(Collectors.toList())
                    );
                    botClient.update(request).subscribe();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
