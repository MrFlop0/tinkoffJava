package edu.java.service.jpa;

import edu.java.domain.dto.Link;
import edu.java.domain.entity.LinkEntity;
import edu.java.domain.repository.jpa.JpaLinkRepository;
import edu.java.service.LinkUpdater;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JpaLinkUpdater implements LinkUpdater {

    private final JpaLinkRepository linkRepository;

    @Override
    public void updateCheckDate(String link) {
        var data = linkRepository.findByLink(link);
        if (data != null) {
            data.setPreviousCheck(OffsetDateTime.now());
            linkRepository.save(data);
        }
    }

    @Override
    public boolean refreshUpdateDate(String link, OffsetDateTime time) {
        var data = linkRepository.findByLink(link);
        if (data != null) {
            data.setPreviousCheck(time);
            linkRepository.save(data);
            return true;
        }
        return false;
    }

    @Override
    public List<Link> getLinksToCheck() {
        return linkRepository.findLinksToCheck().stream().map(LinkEntity::toDto).toList();
    }

    @Override
    public boolean refreshStarsCount(String link, Long count) {
        var data = linkRepository.findByLink(link);
        if (data != null) {
            data.setStarsCount(count);
            linkRepository.save(data);
            return true;
        }
        return false;
    }

    @Override
    public boolean refreshAnswersCount(String link, Long count) {
        var data = linkRepository.findByLink(link);
        if (data != null) {
            data.setAnswerCount(count);
            linkRepository.save(data);
            return true;
        }
        return false;
    }
}
