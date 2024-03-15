package edu.java.controller;

import edu.java.controller.dto.Request.AddLinkRequest;
import edu.java.controller.dto.Request.RemoveLinkRequest;
import edu.java.controller.dto.Response.LinkResponse;
import edu.java.controller.dto.Response.ListLinksResponse;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.utils.GithubLinkParser;
import edu.java.utils.StackoverflowLinkParser;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
public class ScrapperControllerImpl implements ScrapperController {

    private final LinkService linkService;
    private final ChatService chatService;

    @Override
    public void register(@PathVariable Long id) {
        chatService.register(id);
        log.debug("registered chat with id={}", id);
    }

    @Override
    public void unregister(@PathVariable Long id) {
        chatService.unregister(id);
        log.debug("unregistered chat with id={}", id);
    }

    @Override
    public ResponseEntity<ListLinksResponse> getLinks(Long chatId) {
        var links = linkService.findLinksByChat(chatId).stream().map(it -> {
            try {
                return new LinkResponse(chatId, new URI(it.link()));
            } catch (URISyntaxException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        log.debug("fetched track links for chat id={}", chatId);
        return new ResponseEntity<>(new ListLinksResponse(links.size(), links), null, 200);
    }

    @Override
    public ResponseEntity<LinkResponse> addLink(Long chatId, AddLinkRequest request) {
        int type = checkType(request.link());
        if (type == -1) {
            throw new RuntimeException("Link not supported");
        }
        linkService.add(chatId, request.link().toString(), type);
        log.debug("Added link {}\nFor chat id={}", request.link(), chatId);
        return new ResponseEntity<>(new LinkResponse(chatId, request.link()), null, 200);
    }

    private int checkType(URI link) {
        if (GithubLinkParser.parse(link.toString()) != null) {
            return 1;
        } else if (StackoverflowLinkParser.parse(link.toString()) != null) {
            return 0;
        }
        return -1;
    }

    @Override
    public ResponseEntity<LinkResponse> removeLink(Long chatId, RemoveLinkRequest request) {
        linkService.delete(chatId, request.link().toString());
        log.debug("Removed link {}\nFor chat id={}", request.link(), chatId);
        return new ResponseEntity<>(new LinkResponse(chatId, request.link()), null, 200);
    }
}
