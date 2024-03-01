package edu.java.bot.controller;

import edu.java.bot.controller.dto.Request.LinkUpdateRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface BotController {

    void updates(@RequestBody LinkUpdateRequest request);
}
