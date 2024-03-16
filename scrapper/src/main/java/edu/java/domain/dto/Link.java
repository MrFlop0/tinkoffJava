package edu.java.domain.dto;

import java.time.OffsetDateTime;

public record Link(
        String link,

        int type,

        Long starsCount,

        Long answerCount,

        OffsetDateTime updateDate,

        OffsetDateTime previousCheck
) {

}
