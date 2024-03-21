package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import java.sql.Timestamp;
import java.util.List;

public interface LinkRepository {

    List<Link> findAll();

    boolean add(LinkInfo info);

    boolean delete(String link);

    List<Link> findLinksToCheck();

    boolean updateCheckDate(String link);

    boolean updateCheckDate(String link, Timestamp time);

    boolean refreshUpdateDate(String link, Timestamp time);

    boolean refreshStarsCount(String link, Long count);

    boolean refreshAnswersCount(String link, Long count);

}
