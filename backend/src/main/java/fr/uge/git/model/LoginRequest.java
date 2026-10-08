package fr.uge.git.model;

import java.util.Objects;

public record LoginRequest(String token) {
  public LoginRequest{
    Objects.requireNonNull(token);
  }
}
