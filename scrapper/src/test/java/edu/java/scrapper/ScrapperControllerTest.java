package edu.java.scrapper;

import edu.java.controller.ScrapperControllerImpl;
import edu.java.controller.dto.Request.AddLinkRequest;
import edu.java.controller.dto.Request.RemoveLinkRequest;
import edu.java.controller.dto.Response.LinkResponse;
import edu.java.controller.dto.Response.ListLinksResponse;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.service.ChatService;
import edu.java.service.LinkService;
import edu.java.utils.GithubLinkHandler;
import edu.java.utils.StackoverflowLinkHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScrapperControllerTest {

    @Mock
    private LinkService linkService;
    @Mock
    private ChatService chatService;

    @Mock
    private GithubLinkHandler githubLinkHandler;

    @Mock
    private StackoverflowLinkHandler stackoverflowLinkParser;

    private ScrapperControllerImpl scrapperController;

    private void setUpMocks() {
        when(githubLinkHandler.getLinkInfo(eq("https://github.com/owner/repo")))
            .thenReturn(new LinkInfo("https://github.com/owner/repo", 1, null, null));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setUpMocks();
        scrapperController =
            new ScrapperControllerImpl(linkService, chatService, githubLinkHandler, stackoverflowLinkParser);
    }

    @Test
    void register() {
        long id = 1L;
        scrapperController.register(id);
        verify(chatService, times(1)).register(id);
    }

    @Test
    void unregister() {
        long id = 1L;
        scrapperController.unregister(id);
        verify(chatService, times(1)).unregister(id);
    }

    @Test
    void getLinks() throws URISyntaxException {
        long chatId = 1L;
        scrapperController.getLinks(chatId);
        Link test = new Link("http://github.com", 1, OffsetDateTime.now(), OffsetDateTime.now());
        when(linkService.findLinksByChat(chatId)).thenReturn(List.of(test));
        verify(linkService, times(1)).findLinksByChat(chatId);
        assertThat(scrapperController.getLinks(chatId).getBody())
            .isEqualTo(
                new ListLinksResponse(1, List.of(new LinkResponse(1L, new URI("http://github.com"))))
            );

    }

    @Test
    void addLink() {
        long chatId = 1L;
        URI link = URI.create("https://github.com/owner/repo");
        scrapperController.addLink(chatId, new AddLinkRequest(link));
        verify(linkService, times(1)).add(chatId, new LinkInfo(link.toString(), 1, null, null));
    }

    @Test
    void deleteLink() {
        long chatId = 1L;
        URI link = URI.create("https://github.com/owner/repo");
        scrapperController.removeLink(chatId, new RemoveLinkRequest(link));
        verify(linkService, times(1)).delete(chatId, link.toString());

    }

}
