package edu.java.scrapper.utils;

import edu.java.utils.GithubLinkHandler;
import edu.java.utils.StackoverflowLinkHandler;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
public class LinkParserTest {


    @Test
    public void properGithubLink() {
        String link = "https://github.com/user/repo";
        var result = GithubLinkHandler.parse(link);

        assertThat(result).isNotNull();
        assertThat(result.getKey()).isEqualTo("user");
        assertThat(result.getValue()).isEqualTo("repo");
    }

    @Test
    public void invalidGithubLink() {
        String link = "https://github.com";
        var result = GithubLinkHandler.parse(link);
        assertThat(result).isNull();
    }

    @Test
    public void properStackoverflowLink() {
        String link = "https://stackoverflow.com/questions/1";
        var result = StackoverflowLinkHandler.parse(link);
        assertThat(result).isEqualTo(1L);
    }

    @Test
    public void invalidStackoverflowLink() {
        String link = "https://stackoverflow.com/questions";
        var result = StackoverflowLinkHandler.parse(link);
        assertThat(result).isNull();
    }

    @Test
    public void notSupportedLink() {
        String link = "https://google.com/questions/1";
        var result = StackoverflowLinkHandler.parse(link);
        var result2 = GithubLinkHandler.parse(link);
        assertThat(result).isNull();
        assertThat(result2).isNull();
    }
}
