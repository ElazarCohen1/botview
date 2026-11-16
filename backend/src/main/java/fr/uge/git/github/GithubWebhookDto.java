package fr.uge.git.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

/**
 * for representing the result when receving the webhook event from github
 *
 * @param action
 * @param pullRequest
 * @param repository
 */
@Serdeable
@JsonIgnoreProperties(ignoreUnknown = true)
record GitHubWebhookDto(@NotNull String action, @NotNull @JsonProperty("pull_request") PullRequest pullRequest,
                        @NotNull Repository repository) {

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  record PullRequest(int number, @NotNull Ref head, @NotNull Ref base, @JsonProperty("url") String diffUrl) {
  }

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  record Ref(@NotNull String sha, @NotNull String ref) {
  }

  @Serdeable
  @JsonIgnoreProperties(ignoreUnknown = true)
  record Repository(@JsonProperty("full_name") @NotNull String fullName) {
  }
}