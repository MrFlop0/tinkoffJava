package edu.java.domain.repository.jooq;

import edu.java.domain.dto.Link;
import edu.java.domain.dto.LinkInfo;
import edu.java.domain.repository.LinkRepository;
import java.sql.Timestamp;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.springframework.stereotype.Repository;
import static edu.java.domain.jooq.Tables.LINK;
import static org.jooq.impl.DSL.currentLocalDateTime;
import static org.jooq.impl.DSL.localDateTimeSub;

@Repository
@RequiredArgsConstructor
@SuppressWarnings("MagicNumber")
public class JooqLinkRepository implements LinkRepository {

    private final DSLContext dslContext;

    @Override
    public List<Link> findAll() {
        return dslContext.selectFrom(LINK)
            .fetchInto(Link.class);
    }

    @Override
    public boolean add(LinkInfo info) {
        return dslContext.insertInto(LINK)
            .set(LINK.LINK_, info.url())
            .set(LINK.TYPE, info.type())
            .set(LINK.STARS_COUNT, info.starsCount() == null ? null : info.starsCount().intValue())
            .set(LINK.ANSWERS_COUNT, info.answerCount() == null ? null : info.answerCount().intValue())
            .set(LINK.UPDATE_DATE, currentLocalDateTime())
            .set(LINK.PREVIOUS_CHECK, currentLocalDateTime())
            .onConflictDoNothing()
            .execute() != 0;
    }

    @Override
    public boolean delete(String link) {
        return dslContext.deleteFrom(LINK)
            .where(LINK.LINK_.eq(link))
            .execute() != 0;
    }

    @Override
    public List<Link> findLinksToCheck() {
        return dslContext.selectFrom(LINK)
            .where(LINK.PREVIOUS_CHECK.lessThan(localDateTimeSub(currentLocalDateTime(), 60, DatePart.SECOND)))
            .fetchInto(Link.class);
    }

    @Override
    public boolean updateCheckDate(String link) {
        return dslContext.update(LINK)
            .set(LINK.PREVIOUS_CHECK, currentLocalDateTime())
            .where(LINK.LINK_.eq(link))
            .execute() != 0;
    }

    @Override
    public boolean updateCheckDate(String link, Timestamp time) {
        return dslContext.update(LINK)
            .set(LINK.PREVIOUS_CHECK, time.toLocalDateTime())
            .where(LINK.LINK_.eq(link))
            .execute() != 0;
    }

    @Override
    public boolean refreshUpdateDate(String link, Timestamp time) {
        var lastUpdate = getUpdatedDate(link);
        if (lastUpdate != null && time.after(lastUpdate)) {
            dslContext.update(LINK)
                .set(LINK.UPDATE_DATE, time.toLocalDateTime())
                .where(LINK.LINK_.eq(link))
                .execute();
            return true;
        }
        return false;
    }

    @Override
    public boolean refreshStarsCount(String link, Long count) {
        var lastStarsCount = getStarsCount(link);
        if (lastStarsCount != null && !count.equals(lastStarsCount)) {
            dslContext.update(LINK)
                .set(LINK.STARS_COUNT, count.intValue())
                .where(LINK.LINK_.eq(link))
                .execute();
            return true;
        }
        return false;
    }

    @Override
    public boolean refreshAnswersCount(String link, Long count) {
        var lastAnswersCount = getAnswersCount(link);
        if (lastAnswersCount != null && !count.equals(lastAnswersCount)) {
            dslContext.update(LINK)
                .set(LINK.ANSWERS_COUNT, count.intValue())
                .where(LINK.LINK_.eq(link))
                .execute();
            return true;
        }
        return false;
    }

    private Timestamp getUpdatedDate(String link) {
        return dslContext.select(LINK.UPDATE_DATE)
            .from(LINK)
            .where(LINK.LINK_.eq(link))
            .fetchOneInto(Timestamp.class);
    }

    private Long getStarsCount(String link) {
        return dslContext.select(LINK.STARS_COUNT)
            .from(LINK)
            .where(LINK.LINK_.eq(link))
            .fetchOneInto(Long.class);
    }

    private Long getAnswersCount(String link) {
        return dslContext.select(LINK.ANSWERS_COUNT)
            .from(LINK)
            .where(LINK.LINK_.eq(link))
            .fetchOneInto(Long.class);
    }
}
