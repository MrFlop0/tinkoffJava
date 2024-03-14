package edu.java.service;

import edu.java.domain.dto.Link;
import java.time.OffsetDateTime;
import java.util.List;

public interface LinkUpdater {
    void updateCheckDate(String link);

    boolean refreshUpdateDate(String link, OffsetDateTime time);

    List<Link> getLinksToCheck();
}
