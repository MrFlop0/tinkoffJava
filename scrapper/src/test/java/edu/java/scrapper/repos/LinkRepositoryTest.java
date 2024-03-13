package edu.java.scrapper.repos;

import edu.java.configuration.DBConfig;
import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
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
    LinkRepository.class
})
public class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void addLink() {
        linkRepository.add("test", 0);
        linkRepository.add("test1", 1);

        List<Link> links = linkRepository.findAll();

        assertThat(links.size()).isEqualTo(2);

        assertThat(links.getFirst().link()).isEqualTo("test");
        assertThat(links.getFirst().type()).isEqualTo(0);

        assertThat(links.getLast().link()).isEqualTo("test1");
        assertThat(links.getLast().type()).isEqualTo(1);

    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink() {
        linkRepository.add("test", 0);
        linkRepository.delete("test");

        List<Link> links = linkRepository.findAll();

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        linkRepository.add("test", 0);
        linkRepository.add("test1", 0);
        linkRepository.add("test2", 0);

        List<Link> links = linkRepository.findAll();

        assertThat(links.size()).isEqualTo(3);
        assertThat(links.getFirst().link()).isEqualTo("test");
        assertThat(links.get(1).link()).isEqualTo("test1");
        assertThat(links.getLast().link()).isEqualTo("test2");

    }
}
