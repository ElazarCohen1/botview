package fr.uge.git.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public record GitRepoDto(int id, String name, @JsonProperty("html_url") String htmlUrl, @JsonProperty("default_branch") String origin,@JsonProperty("full_name") String fullName) {
    public GitRepoDto{
        Objects.requireNonNull(name);
        Objects.requireNonNull(htmlUrl);
        Objects.requireNonNull(origin);
        Objects.requireNonNull(fullName);
    }
}
