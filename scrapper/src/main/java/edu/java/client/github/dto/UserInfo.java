package edu.java.client.github.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public class UserInfo {
    @JsonProperty("login")
    private String login;
    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("followers")
    private Long numberOfFollowers;
    @JsonProperty("created_at")
    private OffsetDateTime createDate;
    @JsonProperty("updated_at")
    private OffsetDateTime lastUpdateDate;

}
