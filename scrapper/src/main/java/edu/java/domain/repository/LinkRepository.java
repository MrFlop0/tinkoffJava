package edu.java.domain.repository;

import edu.java.domain.dto.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
        String request = "insert into link (link, type, update_date) values (?, ?, now()) on conflict do nothing";
        return jdbcTemplate.update(request, link, type) != 0;
    }

    public boolean delete(String link) {
        String request = "delete from link where link = ?";
        return jdbcTemplate.update(request, link) != 0;
    }
}
