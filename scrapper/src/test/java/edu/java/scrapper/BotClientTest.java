package edu.java.scrapper;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.bot.BotClient;
import edu.java.client.bot.dto.Request.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
class BotClientTest {

    private final BotClient botClient = new BotClient(WebClient.create("http://localhost:8029"));

    private final LinkUpdateRequest linkUpdateRequest = new LinkUpdateRequest(
        1L,
        URI.create("https://www.google.com"),
        "",
        List.of()
    );
    private final static String FAIL_RESPONSE =
        "{\"description\":\"\",\"code\":\"400\",\"exceptionMessage\":\"Invalid request with code 400\"}";

    @Test
    void updateLinkSuccessfully() {
        stubFor(WireMock.post("/updates")
            .willReturn(aResponse().withStatus(200)));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .verifyComplete();
    }

    @Test
    void updateLinkApiError() {
        stubFor(WireMock.post("/updates")
            .willReturn(aResponse().withStatus(400)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(FAIL_RESPONSE)));

        StepVerifier.create(botClient.update(linkUpdateRequest))
            .expectErrorSatisfies(throwable ->
                assertThat(throwable)
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Invalid request with code 400")
            ).verify();
    }

}
