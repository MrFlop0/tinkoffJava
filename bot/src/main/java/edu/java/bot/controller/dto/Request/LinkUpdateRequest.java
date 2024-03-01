package edu.java.bot.controller.dto.Request;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(
    Long id,

    URI url,

    String description,

    @NotNull
    List<Long> tgChatIds
) {
}
