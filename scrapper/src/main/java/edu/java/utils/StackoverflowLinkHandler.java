package edu.java.utils;

import edu.java.client.stackoverflow.StackOverflowClient;
import edu.java.domain.dto.LinkInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("MagicNumber")
@RequiredArgsConstructor
public class StackoverflowLinkHandler {
    private final StackOverflowClient client;

    public static Long parse(String link) {
        String[] parts = link.split("/");
        if (parts.length < 5 || !parts[0].equals("https:") || !parts[2].equals("stackoverflow.com")) {
            return null;
        }
        return Long.parseLong(parts[4]);
    }

    public LinkInfo getLinkInfo(String link) {
        var info = client.getQuestionInfo(parse(link)).block();
        if (info == null) {
            return null;
        }
        return new LinkInfo(link, 0, null, info.items.getFirst().answerCount());
    }
}

