package edu.java.scrapper.service.jdbc;

import edu.java.configuration.DBConfig;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerConfig.class,
    DBConfig.class,
    LinkRepository.class,
    JdbcLinkService.class,
    LinkToChatRepository.class,
    ChatRepository.class
})
public class JdbcLinkServiceTest extends IntegrationTest {

    @Autowired
    private JdbcLinkService linkService;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private LinkToChatRepository linkToChatRepository;

    @Test
    @Transactional
    @Rollback
    public void add() {
        assertThat(linkRepository.findAll()).isEmpty();
        assertThat(linkToChatRepository.findAll()).isEmpty();
        assertThat(chatRepository.findAll()).isEmpty();

        chatRepository.add(0L);
        linkService.add(0, new LinkInfo("test", 1, null, null));

        assertThat(linkRepository.findAll()).isNotEmpty();
        assertThat(linkRepository.findAll().getFirst().link()).isEqualTo("test");
        assertThat(linkRepository.findAll().getFirst().type()).isEqualTo(1);

        assertThat(linkToChatRepository.findAll()).isNotEmpty();
        assertThat(linkToChatRepository.findAll().getFirst().link().link()).isEqualTo("test");
        assertThat(linkToChatRepository.findAll().getFirst().chat().chatId()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void delete() {
        linkRepository.add(new LinkInfo("test", 0, null, null));
        chatRepository.add(1L);
        linkToChatRepository.add("test", 1L);

        assertThat(linkRepository.findAll()).isNotEmpty();
        assertThat(linkToChatRepository.findAll()).isNotEmpty();

        linkService.delete(1L, "test");

        assertThat(linkRepository.findAll()).isEmpty();
        assertThat(linkToChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findLinksByChat() {
        chatRepository.add(1L);
        linkService.add(1L, new LinkInfo("test", 1, null, null));

        assertThat(linkService.findLinksByChat(1L)).isNotEmpty();
        assertThat(linkService.findLinksByChat(1L).getFirst().link()).isEqualTo("test");
        assertThat(linkService.findLinksByChat(0L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByLink() {
        chatRepository.add(1L);
        linkService.add(1L, new LinkInfo("test", 1, null, null));

        assertThat(linkService.findChatsByLink("test")).isNotEmpty();
        assertThat(linkService.findChatsByLink("test").getFirst().chatId()).isEqualTo(1L);
        assertThat(linkService.findChatsByLink("test1")).isEmpty();
    }

}
