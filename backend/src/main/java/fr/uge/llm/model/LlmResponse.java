package fr.uge.llm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Serdeable
public record LlmResponse(@JsonProperty("global_comment") @NotNull String globalComment, @NotNull List<LlmIssue> issues) {}
