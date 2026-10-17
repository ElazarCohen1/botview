package fr.uge.model;


import fr.uge.git.GitProviderType;
import io.micronaut.serde.annotation.Serdeable;
import java.util.Objects;

@Serdeable
public record TokenRequest(GitProviderType provider,String token) {
  public TokenRequest{
    Objects.requireNonNull(provider);
    Objects.requireNonNull(token);
  }
}
