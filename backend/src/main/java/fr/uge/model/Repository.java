package fr.uge.model;

import fr.uge.enums.GitProvider;

import java.util.Objects;

public record Repository(int id, String name, String url, GitProvider gitProvider, String branche) {
  public Repository {
    Objects.requireNonNull(name);
    Objects.requireNonNull(url);
    Objects.requireNonNull(gitProvider);
    Objects.requireNonNull(branche);
  }
}
