package fr.uge.git.model;

import fr.uge.git.GitProviderType;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

/**
 * the global representation after receiving a git webhook
 *
 * @param provider the provider who lunch the webhook
 * @param repo     the repo full name
 * @param baseSha  the commit before the pull request
 * @param headSha  the head commit of the pull request
 * @param action   the event triggered (opened, closed, new push , ...)
 */
@Serdeable
public record GitWebhookDto(@NotNull GitProviderType provider, long prNumber, @NotNull String repo,
                            @NotNull String PullRequestUrl, @NotNull String baseSha,
                            @NotNull String headSha, @NotNull String action, @NotNull String repoUrl) {

}
