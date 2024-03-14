package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.httpclient.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import static java.lang.System.lineSeparator;

@Component
@RequiredArgsConstructor
public class ListCommand implements Command {

    private final ScrapperClient scrapperClient;
    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "List of tracked links";
    }

    @Override
    public SendMessage handle(Update update) {
        return scrapperClient.getLinks(update.message().chat().id()).map(
            response -> new SendMessage(update.message().chat().id(),
                response.links().stream().map(link -> link.url().toString()).reduce((a, b) -> a + lineSeparator() + b)
                    .orElse("No links are being tracked")
            )
        ).onErrorResume(error -> Mono.just(new SendMessage(update.message().chat().id(), error.getMessage()))).block();
        //return new SendMessage(update.message().chat().id(), "List command not supported yet.");
    }
}
