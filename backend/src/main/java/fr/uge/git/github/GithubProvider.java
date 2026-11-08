package fr.uge.git.github;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Objects;

@Singleton
public class GithubProvider implements GitProvider {
  private final GithubClient client;
  private final String token;

  public GithubProvider(GithubClient client, @Value("${github.token:}") String token) {
    Objects.requireNonNull(client);
    Objects.requireNonNull(token);
    this.client = client;
    this.token = token;
    super();
  }

  private void requireToken() {
    if (token.isBlank()) {
      throw new HttpStatusException(HttpStatus.SERVICE_UNAVAILABLE,
              "Token GitHub non configuré (variable d'environnement GITHUB_TOKEN)");
    }
  }

  @Override
  public GitUserDto getUser() {
    return client.getUser(this.token);
  }

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public void addRepository(String repoName) {
    Objects.requireNonNull(repoName);
    client.addRepository(repoName, token);
  }

  @Override
  public HttpResponse<?> removeWebhookFromRepository(int repoId) {
    return client.removeWebhookFromRepository(repoId, token);
  }

  @Override
  public List<GitRepoDto> getRepositories() {
    return client.getRepositories(this.token);
  }

  @Override
  public List<GitPullRequestDto> getPullRequests(String repoName) {
    Objects.requireNonNull(repoName);
    return client.getPullRequests(repoName, this.token);
  }

  @Override
  public List<WebHooksDto> getWebHooks(String repoName) {
    Objects.requireNonNull(repoName);
    return client.getWebHooks(repoName, this.token);
  }


  @Override
  public GitRepoDto getRepository(String repoName) {
    Objects.requireNonNull(repoName);
    return client.getRepository(repoName, this.token);
  }

}
