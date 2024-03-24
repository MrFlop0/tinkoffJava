package edu.java.domain.entity;

import edu.java.domain.dto.Link;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "link")
public class LinkEntity {

    @Id
    @Column(name = "link")
    String link;

    @Column(name = "type")
    int type;

    @Column(name = "stars_count")
    Long starsCount;

    @Column(name = "answers_count")
    Long answerCount;

    @Column(name = "update_date")
    OffsetDateTime updateDate;

    @Column(name = "previous_check")
    OffsetDateTime previousCheck;

    @ManyToMany(mappedBy = "links", fetch = FetchType.EAGER)
    private Set<ChatEntity> chats = new HashSet<>();

    public Link toDto() {
        return new Link(link, type, starsCount, answerCount, updateDate, previousCheck);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o instanceof LinkEntity link) {
            return this.link.equals(link.link)
                && answerCount.equals(link.answerCount)
                && starsCount.equals(link.starsCount)
                && previousCheck.equals(link.previousCheck)
                && updateDate.equals(link.updateDate);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, answerCount, starsCount, previousCheck, updateDate);
    }

}
