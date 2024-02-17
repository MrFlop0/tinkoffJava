package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UntrackCommand implements Command {
    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "Untrack link";
    }

    @Override
    public boolean supports(Update update) {
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
        return new SendMessage(update.message().chat().id(), "Untrack command not supported yet.");
    }
}
