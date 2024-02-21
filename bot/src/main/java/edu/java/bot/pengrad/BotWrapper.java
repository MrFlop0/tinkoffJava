package edu.java.bot.pengrad;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import edu.java.bot.pengrad.command.Command;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotWrapper {

    private final TelegramBot bot;
    private final UpdateListener listener;

    public void startBot() {
        initMenu();
        bot.setUpdatesListener(listener);
    }

    public void stopBot() {
        bot.removeGetUpdatesListener();
    }


    private void initMenu() {
         BotCommand[] menu = listener.commands.stream()
             .map(Command::toApiCommand)
             .toArray(BotCommand[]::new);
         bot.execute(new SetMyCommands(menu));
    }
}
