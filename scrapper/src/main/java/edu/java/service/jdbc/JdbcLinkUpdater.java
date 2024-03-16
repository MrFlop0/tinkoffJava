package edu.java.service.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.repository.LinkRepository;
import edu.java.service.LinkUpdater;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements LinkUpdater {

    private final LinkRepository linkRepository;

    @Override
    public void updateCheckDate(String link) {
        linkRepository.updateCheckDate(link);
    }

    @Override
    public boolean refreshUpdateDate(String link, OffsetDateTime time) {
        var timestamp = Timestamp.valueOf(time.atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
        return linkRepository.refreshUpdateDate(link, timestamp);
    }

    @Override
    public List<Link> getLinksToCheck() {
        return linkRepository.findLinksToCheck();
    }

    @Override
    public boolean refreshStarsCount(String link, Long count) {
        return linkRepository.refreshForksCount(link, count);
    }

    @Override
    public boolean refreshAnswersCount(String link, Long count) {
        return linkRepository.refreshAnswersCount(link, count);
    }
}
