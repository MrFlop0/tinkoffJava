package edu.java.domain.repository.jpa;

import edu.java.domain.entity.LinkEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {

    LinkEntity findByLink(String link);

    @Query(value = "select * from link where link.previous_check < now() - interval '1 minutes'", nativeQuery = true)
    List<LinkEntity> findLinksToCheck();

}
