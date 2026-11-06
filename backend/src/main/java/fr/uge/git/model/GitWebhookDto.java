package fr.uge.git.model;

import fr.uge.git.GitProviderType;
import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

/**
 * the global representation after receiving a git webhook
 * @param provider the provider who lunch the webhook
 * @param repo the repo full name
 * @param baseSha the commit before the pull request
 * @param headSha the head commit of the pull request
 * @param action the event triggered (opened, closed, new push , ...)
 */
@Serdeable
public record GitWebhookDto(GitProviderType provider, String repo, String baseSha, String headSha, String action){
  public GitWebhookDto{
    Objects.requireNonNull(provider);
    Objects.requireNonNull(repo);
    Objects.requireNonNull(baseSha);
    Objects.requireNonNull(headSha);
    Objects.requireNonNull(action);

  }
}
