package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.httpclient.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {

    private final ScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "Track link";
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
        return "Track link.\n Usage: /track <link>";
    }

    @Override
    public SendMessage handle(Update update) {
        var message = update.message().text().split(" ");
        if (message.length != 2) {
            return new SendMessage(update.message().chat().id(), "Invalid command. Usage: /track <link>");
        }

        return scrapperClient.addLink(update.message().chat().id(), message[1])
            .map(response -> new SendMessage(
                    update.message().chat().id(),
                    "Link " + response.url() + " successfully added for tracking"
                )
            )
            .onErrorResume(error -> Mono.just(new SendMessage(update.message().chat().id(), error.getMessage())))
            .block();
        //return new SendMessage(update.message().chat().id(), "Track command not supported yet.");
    }
}
