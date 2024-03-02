package edu.java.client.stackoverflow;

import edu.java.client.BaseClient;
import edu.java.client.stackoverflow.dto.QuestionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StackOverflowClient extends BaseClient {
    private final WebClient stackOverflowWebClient;

    public Mono<QuestionInfo> getQuestionInfo(Long id) {
        String uri = String.format("/2.3/questions/%d?order=desc&sort=activity&site=stackoverflow", id);
        return processGetQuery(uri, QuestionInfo.class);
    }

    @Override
    protected WebClient getWebClient() {
        return stackOverflowWebClient;
    }
}
