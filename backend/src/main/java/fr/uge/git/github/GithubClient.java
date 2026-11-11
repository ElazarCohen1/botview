package fr.uge.git.github;

import fr.uge.database.Database;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.*;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


@Singleton
public class GithubClient {
  private final BlockingHttpClient client;
  private final ObjectMapper mapper;
  private final Database db;

  public GithubClient(@Client("https://api.github.com") HttpClient client, ObjectMapper mapper,Database db) {
    Objects.requireNonNull(client);
    Objects.requireNonNull(mapper);
    Objects.requireNonNull(db);
    this.mapper = mapper;
    this.db = db;
    this.client = client.toBlocking();
    super();
  }

  private HttpRequest<?> get(String path, String token) {
    Objects.requireNonNull(path);
    Objects.requireNonNull(token);

    return HttpRequest.GET(path)
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "pr-review-bot");
  }

  private HttpRequest<?> post(String path, String token, Map<String, Object> body) {
    Objects.requireNonNull(path);
    Objects.requireNonNull(token);

    return HttpRequest.POST(path, body)
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "pr-review-bot");
  }

  private HttpRequest<?> delete(String path, String token) {
    Objects.requireNonNull(path);
    Objects.requireNonNull(token);

    return HttpRequest.DELETE(path)
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "pr-review-bot");
  }

  public GitUserDto getUser(String token) {
    Objects.requireNonNull(token);
    return client.retrieve(get("/user", token), GitUserDto.class);
  }

  public List<GitRepoDto> getRepositories(String token) {
    Objects.requireNonNull(token);
    return client.retrieve(get("/user/repos", token), Argument.listOf(GitRepoDto.class));
  }

  public List<GitPullRequestDto> getPullRequests(String repoFullName, String token) {
    Objects.requireNonNull(repoFullName);
    Objects.requireNonNull(token);
    String path = "/repos/" + repoFullName + "/pulls";
    return client.retrieve(get(path, token), Argument.listOf(GitPullRequestDto.class));
  }

  public GitRepoDto getRepository(String repoFullName, String token) {
    Objects.requireNonNull(repoFullName);
    Objects.requireNonNull(token);
    String path = "/repos/" + repoFullName;
    return client.retrieve(get(path, token), GitRepoDto.class);
  }

  public List<WebHooksDto> getWebHooks(String repoFullName, String token) {
    Objects.requireNonNull(repoFullName);
    Objects.requireNonNull(token);
    String path = "/repos/" + repoFullName + "/hooks";
    return client.retrieve(get(path, token), Argument.listOf(WebHooksDto.class));
  }

  public WebHooksDto createWebhook(String repoName, String token) {
    Objects.requireNonNull(repoName);
    Objects.requireNonNull(token);
    String path = "/repos/" + repoName + "/hooks";
    Map<String, Object> body = Map.of(
            "name", "web",
            "active", true,
            "events", List.of("pull_request"),
            "config", Map.of(
                    "url", "https://upscale-sixfold-extruding.ngrok-free.dev/api/hooks/receive/GITHUB",
                    "content_type", "json",
                    "insecure_ssl", "0"));
    return client.retrieve(post(path, token, body), WebHooksDto.class);
  }



  public HttpResponse<?> removeWebhookFromRepository(int repoId, String token) {
    Objects.requireNonNull(token);

    var repositoryQueries = db.getRepositoryQueries();
    try {
      var webhookId = repositoryQueries.getWebhookId(repoId).orElseThrow();
      var repo = repositoryQueries.getRepository(repoId).orElseThrow();

      String path = "/repos/" + repo.name() + "/hooks/" + webhookId;
      try {
        client.exchange(delete(path, token));
      } catch (HttpClientResponseException e) {
        if (e.getStatus() != HttpStatus.NOT_FOUND) {
          return HttpResponse.status(e.getStatus());
        }
      }
      repositoryQueries.removeWebhookFromRepository(repoId);
      return HttpResponse.noContent();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void addRepository(String repoName, String token) {
    Objects.requireNonNull(repoName);
    var repository = getRepository(repoName, token);
    var repoQueries = db.getRepositoryQueries();
    try {
      var existingRepo = repoQueries.findByUrl(repository.htmlUrl());
      if (existingRepo.isPresent() && existingRepo.get().has_webhook()) {
        return;
      }
      var webhook = createWebhook(repoName, token);
      var webhookId = String.valueOf(webhook.id());
      if (existingRepo.isPresent()) {
        repoQueries.setWebhook(existingRepo.get().id(), webhookId);
      } else {
        repoQueries.addRepository(repository.fullName(), webhookId, repository.htmlUrl(), GitProviderType.GITHUB);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

  }

  public Optional<GitWebhookDto> parseWebhook(HttpHeaders headers, String rawBody) {
    Objects.requireNonNull(headers);
    Objects.requireNonNull(rawBody);

    if (!"pull_request".equals(headers.get("X-GitHub-Event"))) {
      return Optional.empty();
    }
    try {
      var dto = mapper.readValue(rawBody, GitHubWebhookDto.class);
      if (dto == null) {
        return Optional.empty();
      }
      return Optional.of(
              new GitWebhookDto(
                      GitProviderType.GITHUB,
                      dto.repository().fullName(),
                      dto.pullRequest().base().sha(),
                      dto.pullRequest().head().sha(),
                      dto.action()
              )
      );
    } catch (IOException e) {
      throw new IllegalArgumentException("Webhook GitHub invalide", e);
    }
  }

}


