package edu.java.bot.pengrad;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.pengrad.command.Command;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateListener implements UpdatesListener {

    protected final List<Command> commands;
    private final TelegramBot bot;

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> commands.stream()
            .filter(command -> command.supports(update))
            .findFirst()
            .ifPresentOrElse(
                command -> bot.execute(command.handle(update)),
                () -> bot.execute(new SendMessage(update.message().chat().id(), "Unknown command"))
            ));

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
