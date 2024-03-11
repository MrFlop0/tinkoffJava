package edu.java.bot.httpclient.dto.Request;

import java.net.URI;

public record RemoveLinkRequest(
    URI link
) {
}
