package edu.java.scrapper.repos.jdbc;

import edu.java.configuration.DBConfig;
import edu.java.domain.dto.Chat;
import edu.java.domain.repository.jdbc.JdbcChatRepository;
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
    JdbcChatRepository.class
})
public class JdbcChatRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void addChat() {
        chatRepository.add(1L);

        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(1);
        assertThat(chats.getFirst().chatId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteChat() {
        chatRepository.add(2L);
        chatRepository.delete(2L);

        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        chatRepository.add(4L);
        chatRepository.add(5L);

        List<Chat> chats = chatRepository.findAll();

        assertThat(chats.size()).isEqualTo(5);
    }

}
