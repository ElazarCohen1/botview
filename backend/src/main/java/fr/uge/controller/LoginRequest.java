package fr.uge.controller;

import java.util.Objects;

public record LoginRequest(String token) {
  public LoginRequest{
    Objects.requireNonNull(token);
  }
}
