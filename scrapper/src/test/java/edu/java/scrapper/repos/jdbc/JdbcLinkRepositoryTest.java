package edu.java.scrapper.repos.jdbc;

import edu.java.configuration.DBConfig;
import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.jdbc.JdbcLinkRepository;
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
    JdbcLinkRepository.class
})
public class JdbcLinkRepositoryTest {

    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void addLink() {
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.add(new LinkInfo("test1", 1, null, null));

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
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.delete("test");

        List<Link> links = linkRepository.findAll();

        assertThat(links.size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllTest() {
        linkRepository.add(new LinkInfo("test", 0, null, null));
        linkRepository.add(new LinkInfo("test1", 0, null, null));
        linkRepository.add(new LinkInfo("test2", 0, null, null));

        List<Link> links = linkRepository.findAll();

        assertThat(links.size()).isEqualTo(3);
        assertThat(links.getFirst().link()).isEqualTo("test");
        assertThat(links.get(1).link()).isEqualTo("test1");
        assertThat(links.getLast().link()).isEqualTo("test2");

    }
}
