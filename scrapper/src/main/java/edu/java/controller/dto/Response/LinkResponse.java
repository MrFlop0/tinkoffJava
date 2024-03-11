package edu.java.controller.dto.Response;

import java.net.URI;

public record LinkResponse(
    Long id,
    URI url
) {
}
