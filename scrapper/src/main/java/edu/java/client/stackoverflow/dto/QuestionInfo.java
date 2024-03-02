package edu.java.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public class QuestionInfo {

    @JsonProperty("items")
    public List<Item> items;
    @JsonProperty("has_more")
    public Boolean hasMore;

    public record Item(
        List<String> tags,
        Owner owner,
        @JsonProperty("creation_date") OffsetDateTime creationDate,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
        @JsonProperty("question_id") Long questionId,
        @JsonProperty("is_answered") boolean isAnswered,
        @JsonProperty("answer_count") Integer answerCount
    ) {
    }

    public record Owner(
        @JsonProperty("account_id") Long accountId,
        @JsonProperty("user_id") Long userId
    ) {
    }

}
