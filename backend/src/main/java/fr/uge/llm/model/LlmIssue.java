package fr.uge.llm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record LlmIssue(
        @NotNull String file,
        int line,
        @NotNull String rule,
        @NotNull  String description,
        @JsonProperty("is_blocking")
        boolean blocking
) {}
