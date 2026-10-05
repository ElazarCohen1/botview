package fr.uge.git.model;

import fr.uge.git.GitProviderType;

import java.util.Objects;

public record RepositoryDto(int id, String name, String url, GitProviderType gitProviderType, String branche) {
  public RepositoryDto {
    Objects.requireNonNull(name);
    Objects.requireNonNull(url);
    Objects.requireNonNull(gitProviderType);
    Objects.requireNonNull(branche);
  }
}
