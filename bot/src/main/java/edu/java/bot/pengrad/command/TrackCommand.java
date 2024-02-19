package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class TrackCommand implements Command {
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
        return new SendMessage(update.message().chat().id(), "Track command not supported yet.");
    }
}
