package fr.uge.git.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

// to transform this record into json and json -> record
@Serdeable
public record GitUserDto(String login, long id, @JsonProperty("avatar_url") String avatarUrl) {
  public GitUserDto {
    Objects.requireNonNull(login);
    Objects.requireNonNull(avatarUrl);
  }
}
