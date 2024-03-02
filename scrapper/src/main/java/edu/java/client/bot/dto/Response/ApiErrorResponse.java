package edu.java.client.bot.dto.Response;

import java.util.List;

public record ApiErrorResponse(
    String description,
    String code,
    String exceptionName,
    String exceptionMessage,
    List<String> stacktrace
) {
}
