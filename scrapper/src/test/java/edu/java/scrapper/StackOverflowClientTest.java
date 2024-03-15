package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.stackoverflow.StackOverflowClient;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8029)
public class StackOverflowClientTest {

    private final StackOverflowClient client = new StackOverflowClient(WebClient.create("http://localhost:8029"));

    @Test
    public void testStackOverflowResponse() {
        stubFor(get(urlEqualTo("/questions/1?order=desc&sort=activity&site=stackoverflow")).willReturn(
                aResponse()
                    .withBodyFile("StackOverflowJSON.json")
                    .withStatus(200)
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            )
        );

        StepVerifier
            .create(client.getQuestionInfo(1L))
            .assertNext(
                questionInfo -> {
                    assertThat(questionInfo.hasMore).isEqualTo(false);
                    assertThat(questionInfo.items.size()).isEqualTo(1);
                    var item = questionInfo.items.getFirst();
                    assertThat(item.isAnswered()).isEqualTo(true);
                    assertThat(item.creationDate()).isEqualTo( "2008-10-21T17:46:29Z");
                    assertThat(item.owner().accountId()).isEqualTo(14554);
                }
            )
            .verifyComplete();
    }

    @Test
    public void testFailedRequest() {
        stubFor(get("/2.3/questions/1?order=desc&sort=activity&site=stackoverflow").willReturn(aResponse().withStatus(404)));

        StepVerifier.create(client.getQuestionInfo(1L))
            .expectErrorSatisfies(
                error -> {
                    assertThat(error)
                        .isInstanceOf(WebClientResponseException.class)
                        .hasMessage("400 Server Error occured");
                    assertThat(((WebClientResponseException) error).getStatusCode().is4xxClientError())
                        .isEqualTo(true);
                }
            )
            .verify();
    }
}
