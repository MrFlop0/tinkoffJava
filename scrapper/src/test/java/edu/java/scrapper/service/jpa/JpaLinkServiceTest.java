package edu.java.scrapper.service.jpa;

import edu.java.domain.dto.LinkInfo;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.ServiceConfig;
import edu.java.service.jpa.JpaLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(excludeAutoConfiguration = LiquibaseAutoConfiguration.class)
@Import({IntegrationTest.JpaConfig.class, ServiceConfig.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaLinkServiceTest extends IntegrationTest {

    @Autowired
    private JpaLinkService linkService;


    @Test
    public void add() {
        linkService.add(0, new LinkInfo("test", 0, null, null));

        assertThat(linkService.findLinksByChat(0)).isNotEmpty();
        assertThat(linkService.findLinksByChat(0).getFirst().link()).isEqualTo("test");
        assertThat(linkService.findLinksByChat(0).getFirst().type()).isEqualTo(0);
    }

    @Test
    public void delete() {
        linkService.add(0, new LinkInfo("test", 0, null, null));

        assertThat(linkService.findLinksByChat(0)).isNotEmpty();
        assertThat(linkService.findLinksByChat(0).getFirst().link()).isEqualTo("test");
        assertThat(linkService.findLinksByChat(0).getFirst().type()).isEqualTo(0);

        linkService.delete(0L, "test");

        assertThat(linkService.findLinksByChat(0)).isEmpty();
    }

    @Test
    public void findLinksByChat() {
        linkService.add(1L, new LinkInfo("test", 1, null, null));

        assertThat(linkService.findLinksByChat(1L)).isNotEmpty();
        assertThat(linkService.findLinksByChat(1L).getFirst().link()).isEqualTo("test");
        assertThat(linkService.findLinksByChat(0L)).isEmpty();
    }

    @Test
    public void findChatsByLink() {
        linkService.add(1L, new LinkInfo("test", 1, null, null));

        assertThat(linkService.findChatsByLink("test")).isNotEmpty();
        assertThat(linkService.findChatsByLink("test").getFirst().chatId()).isEqualTo(1L);
        assertThat(linkService.findChatsByLink("test1")).isEmpty();
    }

}
