package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class StartCommand implements Command {
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
        return new SendMessage(update.message().chat().id(), greeting);
    }
}
