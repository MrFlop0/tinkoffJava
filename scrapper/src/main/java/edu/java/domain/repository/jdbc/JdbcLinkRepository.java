package edu.java.domain.repository.jdbc;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.LinkRepository;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {

    private final RowMapper<Link> linkRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public List<Link> findAll() {
        String request = "select * from link";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    public boolean add(LinkInfo info) {
        String request =
            "insert into link (link, type, stars_count, answers_count, update_date, previous_check) "
                + "values (?, ?, ?, ?, timezone('utc', now()), timezone('utc', now())) on conflict do nothing";
        return jdbcTemplate.update(request, info.url(), info.type(), info.starsCount(), info.answerCount()) != 0;
    }

    public boolean delete(String link) {
        String request = "delete from link where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    public List<Link> findLinksToCheck() {
        String request = "select * from link where previous_check < timezone('utc', now()) - interval '1 minute'";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    public boolean updateCheckDate(String link) {
        String request = "update link set previous_check = timezone('utc', now()) where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    public boolean updateCheckDate(String link, Timestamp time) {
        String request = "update link set previous_check = ? where link = ?";

        var lastCheck = getLastCheckDate(link);
        if (lastCheck != null && time.after(lastCheck)) {
            jdbcTemplate.update(request, time, link);
            return true;
        }
        return false;
    }

    public boolean refreshUpdateDate(String link, Timestamp time) {
        String request = "update link set update_date = ? where link = ?";

        var lastUpdate = getUpdatedDate(link);
        if (lastUpdate != null && time.after(lastUpdate)) {
            jdbcTemplate.update(request, time, link);
            return true;
        }
        return false;
    }

    public boolean refreshStarsCount(String link, Long count) {
        String request = "update link set stars_count = ? where link = ?";

        var lastForksCount = getForksCount(link);
        if (lastForksCount != null && !count.equals(lastForksCount)) {
            jdbcTemplate.update(request, count, link);
            return true;
        }

        return false;
    }

    public boolean refreshAnswersCount(String link, Long count) {
        String request = "update link set answers_count = ? where link = ?";

        var lastAnswersCount = getAnswersCount(link);
        if (lastAnswersCount != null && !count.equals(lastAnswersCount)) {
            jdbcTemplate.update(request, count, link);
            return true;
        }

        return false;
    }

    private Timestamp getUpdatedDate(String link) {
        String request = "select update_date from link where link = ?";
        return jdbcTemplate.queryForObject(request, Timestamp.class, link);
    }

    private Timestamp getLastCheckDate(String link) {
        String request = "select previous_check from link where link = ?";
        return jdbcTemplate.queryForObject(request, Timestamp.class, link);
    }

    private Long getForksCount(String link) {
        String request = "select stars_count from link where link = ?";
        return jdbcTemplate.queryForObject(request, Long.class, link);
    }

    private Long getAnswersCount(String link) {
        String request = "select answers_count from link where link = ?";
        return jdbcTemplate.queryForObject(request, Long.class, link);
    }

}
