package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.httpclient.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {

    private final ScrapperClient scrapperClient;
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Untrack link";
    }

    @Override
    public boolean isSupported(Update update) {
        if (update.message().text() == null) {
            return false;
        }
        return update.message().text().split(" ")[0].equals(command());
    }

    @Override
    public String helpInfo() {
        return "Untrack link.\n Usage: /untrack <link>";
    }

    @Override
    public SendMessage handle(Update update) {
        var message = update.message().text().split(" ");
        if (message.length != 2) {
            return new SendMessage(update.message().chat().id(), "Invalid command. Usage: /untrack <link>");
        }

        return scrapperClient.removeLink(update.message().chat().id(), message[1])
            .map(response -> new SendMessage(update.message().chat().id(), "Link " + response.url() + " successfully removed from tracking"))
            .onErrorResume(error -> Mono.just(new SendMessage(update.message().chat().id(), error.getMessage())))
            .block();
        //return new SendMessage(update.message().chat().id(), "Untrack command not supported yet.");
    }
}
