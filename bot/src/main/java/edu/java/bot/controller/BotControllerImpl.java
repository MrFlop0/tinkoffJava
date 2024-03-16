package edu.java.bot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.dto.Request.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BotControllerImpl implements BotController {
    private final TelegramBot bot;

    @Override
    @PostMapping("/updates")
    public void updates(LinkUpdateRequest request) {
        request.tgChatIds().stream()
            .map(id -> new SendMessage(id, request.description()))
            .forEach(bot::execute);
    }
}
