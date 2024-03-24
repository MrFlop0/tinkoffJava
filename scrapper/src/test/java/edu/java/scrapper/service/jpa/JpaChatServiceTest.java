package edu.java.scrapper.service.jpa;

import edu.java.domain.entity.ChatEntity;
import edu.java.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.ServiceConfig;
import edu.java.service.jpa.JpaChatService;
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
class JpaChatServiceTest extends IntegrationTest {
    @Autowired
    private JpaChatRepository chatRepository;
    @Autowired
    private JpaChatService chatService;

    @Test
    public void register() {
        assertThat(chatRepository.findAll()).isEmpty();

        chatService.register(1L);

        assertThat(chatRepository.findAll()).isNotEmpty();
        assertThat(chatRepository.findAll().getFirst().getChatId()).isEqualTo(1L);
    }

    @Test
    public void unregister() {
        var data = new ChatEntity();
        data.setChatId(1L);
        chatRepository.save(data);

        assertThat(chatRepository.findAll()).isNotEmpty();

        chatService.unregister(1L);

        assertThat(chatRepository.findAll()).isEmpty();
    }
}

