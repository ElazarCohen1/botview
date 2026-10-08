package fr.uge.git.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GitPullRequestDto(int number, String title, String state, @JsonProperty ("html_url") String htmlUrl) {
}
