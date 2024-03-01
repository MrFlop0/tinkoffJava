package edu.java.controller;

import edu.java.controller.dto.Request.AddLinkRequest;
import edu.java.controller.dto.Request.RemoveLinkRequest;
import edu.java.controller.dto.Response.LinkResponse;
import edu.java.controller.dto.Response.ListLinksResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ScrapperController {

    void register(@PathVariable Long id);

    void unregister(@PathVariable Long id);

    ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") Long chatId);

    ResponseEntity<LinkResponse> addLinks(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody AddLinkRequest request
    );

    ResponseEntity<LinkResponse> removeLinks(
        @RequestHeader("Tg-Chat-Id") Long chatId,
        @RequestBody RemoveLinkRequest request
    );


}
