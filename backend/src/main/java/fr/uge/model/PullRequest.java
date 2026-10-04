package fr.uge.model;

import java.time.Instant;
import java.util.Objects;

public record PullRequest(int pullId, String commentaire, String name, Instant date) {
  public PullRequest {
    Objects.requireNonNull(commentaire);
    Objects.requireNonNull(name);
    Objects.requireNonNull(date);
  }
}
