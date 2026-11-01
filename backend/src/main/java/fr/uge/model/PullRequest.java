package fr.uge.model;

import java.time.Instant;
import java.util.Objects;

public record PullRequest(int pullId, String name, Instant date) {
  public PullRequest {
    Objects.requireNonNull(name);
    Objects.requireNonNull(date);
  }
}
