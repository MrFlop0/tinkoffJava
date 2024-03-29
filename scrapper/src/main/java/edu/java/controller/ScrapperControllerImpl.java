package edu.java.controller;

import edu.java.controller.dto.Request.AddLinkRequest;
import edu.java.controller.dto.Request.RemoveLinkRequest;
import edu.java.controller.dto.Response.LinkResponse;
import edu.java.controller.dto.Response.ListLinksResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ScrapperControllerImpl implements ScrapperController {
    @Override
    public void register(Long id) {
        log.debug("registered chat with id={}", id);
    }

    @Override
    public void unregister(Long id) {
        log.debug("unregistered chat with id={}", id);
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(Long chatId) {
        log.debug("fetched track links for chat id={}", chatId);
        return null;
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(Long chatId, AddLinkRequest request) {
        log.debug("Added link {}\nFor chat id={}", request.link(), chatId);
        return null;
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(Long chatId, RemoveLinkRequest request) {
        log.debug("Removed link {}\nFor chat id={}", request.link(), chatId);
        return null;
    }
}
