package edu.java.scrapper;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.java.client.github.GitHubClient;
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
public class GitHubClientTest {

    private final GitHubClient client = new GitHubClient(WebClient.create("http://localhost:8029"));

    @Test
    public void testGitHubResponse() {

        stubFor(get(urlEqualTo("/repos/test/test")).willReturn(
                aResponse()
                    .withBodyFile("GithubJSON.json")
                    .withStatus(200)
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            )
        );

        StepVerifier
            .create(client.getRepoInfo("test", "test"))
            .assertNext(
                userInfo -> {
                    assertThat(userInfo.id).isEqualTo(1296269);
                    assertThat(userInfo.createDate).isEqualTo("2011-01-26T19:01:12Z");
                    assertThat(userInfo.updateDate).isEqualTo("2011-01-26T19:14:43Z");
                    //"21.10.2008, 21:46:29"  "2008-10-21T18:46:29Z"
                    assertThat(userInfo.owner.login()).isEqualTo("octocat");
                    assertThat(userInfo.owner.ownerId()).isEqualTo(1);
                }
            )
            .verifyComplete();
    }

    @Test
    public void testFailedRequest() {
        stubFor(get("/repos/test/test").willReturn(aResponse().withStatus(400)));

        StepVerifier.create(client.getRepoInfo("test", "test"))
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
