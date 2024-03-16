package edu.java.scrapper.repos;

import edu.java.configuration.DBConfig;
import edu.java.domain.dto.Chat;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkChat;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.ChatRepository;
import edu.java.domain.repository.LinkRepository;
import edu.java.domain.repository.LinkToChatRepository;
import edu.java.scrapper.IntegrationTest;
import java.util.List;
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
    ChatRepository.class,
    LinkToChatRepository.class
})
public class LinkToChatRepositoryTest {

    @Autowired
    private LinkToChatRepository linkToChatRepository;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void addTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);

        List<LinkChat> mapping = linkToChatRepository.findAll();

        assertThat(mapping.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteTest() {
        chatRepository.add(1L);
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkToChatRepository.add("test", 1L);

        linkToChatRepository.delete("test", 1L);

        List<LinkChat> mapping = linkToChatRepository.findAll();

        assertThat(mapping.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.add(new LinkInfo("test1", 0, null, null));
        linkRepository.add(new LinkInfo("test2", 0, null, null));
        linkRepository.add(new LinkInfo("test3", 0, null, null));
        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test1", 1L);
        linkToChatRepository.add("test2", 2L);
        linkToChatRepository.add("test3", 3L);

        List<LinkChat> mapping = linkToChatRepository.findAll();

        assertThat(mapping.size()).isEqualTo(4);
    }

    @Test
    @Transactional
    @Rollback
    public void findChatsByUrlTest() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.add(new LinkInfo("test1", 0, null, null));
        linkRepository.add(new LinkInfo("test2", 0, null, null));
        linkRepository.add(new LinkInfo("test3", 0, null, null));

        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);
        linkToChatRepository.add("test1", 3L);
        linkToChatRepository.add("test1", 2L);
        linkToChatRepository.add("test2", 1L);
        linkToChatRepository.add("test2", 3L);
        linkToChatRepository.add("test3", 0L);
        linkToChatRepository.add("test3", 2L);

        List<Chat> test = linkToChatRepository.findChatsByLink("test");
        List<Chat> test1 = linkToChatRepository.findChatsByLink("test1");
        List<Chat> test2 = linkToChatRepository.findChatsByLink("test2");
        List<Chat> test3 = linkToChatRepository.findChatsByLink("test3");

        assertThat(test.stream().map(Chat::chatId)).isEqualTo(List.of(0L, 1L));
        assertThat(test1.stream().map(Chat::chatId)).isEqualTo(List.of(3L, 2L));
        assertThat(test2.stream().map(Chat::chatId)).isEqualTo(List.of(1L, 3L));
        assertThat(test3.stream().map(Chat::chatId)).isEqualTo(List.of(0L, 2L));
    }

    @Test
    @Transactional
    @Rollback
    public void findLinksByChat() {
        chatRepository.add(0L);
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.add(new LinkInfo("test1", 0, null, null));
        linkRepository.add(new LinkInfo("test2", 0, null, null));
        linkRepository.add(new LinkInfo("test3", 0, null, null));

        linkToChatRepository.add("test", 0L);
        linkToChatRepository.add("test", 1L);
        linkToChatRepository.add("test1", 3L);
        linkToChatRepository.add("test1", 2L);
        linkToChatRepository.add("test2", 1L);
        linkToChatRepository.add("test2", 3L);
        linkToChatRepository.add("test3", 0L);
        linkToChatRepository.add("test3", 2L);

        List<Link> chat0 = linkToChatRepository.findLinksByChat(0L);
        List<Link> chat1 = linkToChatRepository.findLinksByChat(1L);
        List<Link> chat2 = linkToChatRepository.findLinksByChat(2L);
        List<Link> chat3 = linkToChatRepository.findLinksByChat(3L);

        assertThat(chat0.stream().map(Link::link)).isEqualTo(List.of("test", "test3"));
        assertThat(chat1.stream().map(Link::link)).isEqualTo(List.of("test", "test2"));
        assertThat(chat2.stream().map(Link::link)).isEqualTo(List.of("test1", "test3"));
        assertThat(chat3.stream().map(Link::link)).isEqualTo(List.of("test1", "test2"));
    }

}
