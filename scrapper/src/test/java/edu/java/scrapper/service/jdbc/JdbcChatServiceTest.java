package edu.java.scrapper.service.jdbc;

import edu.java.configuration.DBConfig;
import edu.java.domain.repository.ChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.service.jdbc.JdbcChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {
    IntegrationTest.ManagerConfig.class,
    DBConfig.class,
    ChatRepository.class,
    JdbcChatService.class
})
public class JdbcChatServiceTest extends IntegrationTest {

    @Autowired
    private JdbcChatService chatService;
    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void register() {
        assertThat(chatRepository.findAll()).isEmpty();

        chatService.register(1L);

        assertThat(chatRepository.findAll()).isNotEmpty();
        assertThat(chatRepository.findAll().getFirst().chatId()).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void unregister() {
        chatRepository.add(1L);

        assertThat(chatRepository.findAll()).isNotEmpty();

        chatService.unregister(1L);

        assertThat(chatRepository.findAll()).isEmpty();
    }
}
