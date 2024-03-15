package edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.controller.BotControllerImpl;
import edu.java.bot.controller.dto.Request.LinkUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.net.URI;
import java.util.Arrays;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BotControllerTest {

    @Mock
    private TelegramBot telegramBot;

    private BotControllerImpl botController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        botController = new BotControllerImpl(telegramBot);
    }

    @Test
    void update() {
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            URI.create("https://github.com"),
            "test",
            Arrays.asList(1L, 2L)
        );

        botController.updates(request);

        verify(telegramBot, times(2)).execute(any(SendMessage.class));
    }


}
