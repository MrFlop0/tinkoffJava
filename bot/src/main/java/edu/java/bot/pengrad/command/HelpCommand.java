package edu.java.bot.pengrad.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final List<Command> commands;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "Show help";
    }

    @Override
    public SendMessage handle(Update update) {
        String helpInfo = commands.stream()
            .map(command -> command.command() + " - " + command.helpInfo())
            .reduce((s1, s2) -> s1 + "\n\n" + s2)
            .orElse("No commands available");

        return new SendMessage(update.message().chat().id(), helpInfo);
    }
}
