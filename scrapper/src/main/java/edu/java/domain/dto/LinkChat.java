package edu.java.domain.dto;

import java.time.OffsetDateTime;

public record LinkChat(
    long chatId,

    String link,

    int type,

    Long starsCount,

    Long answerCount,

    OffsetDateTime updateDate,

    OffsetDateTime previousCheck
) {
}
