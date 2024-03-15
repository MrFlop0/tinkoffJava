package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.httpclient.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "Start command";
    }

    @Override
    public SendMessage handle(Update update) {
        String greeting = """
            Hello! I'm a Link Listener bot.
            I'm keep track of updates of provided links.
            Use /help to see available commands
            """;
        return scrapperClient.register(update.message().chat().id())
            .then(Mono.fromCallable(() -> new SendMessage(update.message().chat().id(), greeting)))
            .onErrorResume(error -> Mono.just(
                new SendMessage(
                    update.message().chat().id(),
                    "failed to register, try again later"
                ))
            ).block();
    }
}
