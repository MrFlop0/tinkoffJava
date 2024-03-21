package edu.java.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class RepoInfo {
    @JsonProperty("id")
    public Long id;
    public Owner owner;

    @JsonProperty("stargazers_count")
    public Long starsCount;

    @JsonProperty("created_at")
    public OffsetDateTime createDate;
    @JsonProperty("updated_at")
    public OffsetDateTime updateDate;

    public record Owner(
        @JsonProperty("login") String login,
        @JsonProperty("id") Long ownerId
    ) {
    }

}
