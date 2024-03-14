package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

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
        String request = "insert into link (link, type, update_date, previous_check) values (?, ?, now(), now()) on conflict do nothing";
        return jdbcTemplate.update(request, link, type) != 0;
    }

    public boolean delete(String link) {
        String request = "delete from link where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    public List<Link> findLinksToCheck() {
        String request = "select * from link where previous_check < now() - interval '5 minutes'";
        return jdbcTemplate.query(request, linkRowMapper);
    }

    public boolean updateCheckDate(String link) {
        String request = "update link set previous_check = now() where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }

    public boolean updateCheckDate(String link, Timestamp time) {
        String request = "update link set previous_check = ? where link = ?";
        return jdbcTemplate.update(request, time, link) != 0;
    }

    public boolean refreshUpdateDate(String link, Timestamp time) {
        String request = "update link set update_date = ? where link = ?";
        return jdbcTemplate.update(request, time, link) != 0;
    }
}
