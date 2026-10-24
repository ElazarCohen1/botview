package fr.uge.git.model;

import fr.uge.git.GitProviderType;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public record RepositoryDto(int id, String name, String url, GitProviderType gitProviderType) {
  public RepositoryDto {
    Objects.requireNonNull(name);
    Objects.requireNonNull(url);
    Objects.requireNonNull(gitProviderType);
  }
}
