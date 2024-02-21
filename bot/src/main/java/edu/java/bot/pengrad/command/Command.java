package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String command();

    String description();

    default String helpInfo() {
        return description();
    }

    SendMessage handle(Update update);

    default boolean isSupported(Update update) {
        if (update.message().text() == null) {
            return false;
        }
        return update.message().text().equals(command());
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
