package edu.java.bot.httpclient.dto.Response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
