package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    Urls urls
) {
    public record Scheduler(boolean enable, @NotNull Duration interval, @NotNull Duration forceCheckDelay) {
    }

    public record Urls(
        @NotNull
        @DefaultValue("https://api.github.com")
        String github,
        @NotNull
        @DefaultValue("https://api.stackexchange.com/2.3")
        String stackoverflow,

        @NotNull
        @DefaultValue("http://localhost:8090")
        String bot
    ) {

    }
}
