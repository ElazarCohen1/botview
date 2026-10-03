package fr.uge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

// to transform this record into json and json -> record
@Serdeable
public record GithubUser(String login, long id,@JsonProperty("avatar_url") String avatarUrl) {
  public  GithubUser{
    Objects.requireNonNull(login);
    Objects.requireNonNull(avatarUrl);
  }
}
