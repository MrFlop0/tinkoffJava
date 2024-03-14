package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LinkRepository {

    private final RowMapper<Link> linkRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public List<Link> findAll() {
        String request = "select * from link";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    public boolean add(String link, int type) {
        String request =
            "insert into link (link, type, update_date, previous_check) " +
                "values (?, ?, timezone('utc', now()), timezone('utc', now())) on conflict do nothing";
        return jdbcTemplate.update(request, link, type) != 0;
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

        var last_check = getLastCheckDate(link);
        if (last_check != null && time.after(last_check)) {
            jdbcTemplate.update(request, time, link);
            return true;
        }
        return false;
    }

    public boolean refreshUpdateDate(String link, Timestamp time) {
        String request = "update link set update_date = ? where link = ?";

        var last_update = getUpdatedDate(link);
        if (last_update != null && time.after(last_update)) {
            jdbcTemplate.update(request, time, link);
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
}
