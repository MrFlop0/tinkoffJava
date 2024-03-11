package edu.java.controller.dto.Response;

import java.util.List;

public record ListLinksResponse(
    int size,
    List<LinkResponse> links
) {
}
