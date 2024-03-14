package edu.java.domain.dto;

import java.time.OffsetDateTime;

public record Link(
        String link,
        int type,
        OffsetDateTime updateDate,

        OffsetDateTime previous_check
) {
}
