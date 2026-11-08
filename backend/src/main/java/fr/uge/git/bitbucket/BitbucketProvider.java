package fr.uge.git.bitbucket;

import fr.uge.git.GitProvider;
import fr.uge.git.model.*;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Singleton
public class BitbucketProvider implements GitProvider {

  private final String token;

  public BitbucketProvider(@Value("${bitbucket.token:}") String token) {
    Objects.requireNonNull(token);
    this.token = token;
    super();
  }

  @Override
  public GitUserDto getUser() {
    return new GitUserDto("", 0, "null");
  }

  @Override
  public String getToken() {
    return token;
  }

  @Override
  public void addRepository(String repoName) {

  }

  @Override
  public HttpResponse<?> removeWebhookFromRepository(int repoId) {
    return HttpResponse.ok();
  }

  @Override
  public List<GitRepoDto> getRepositories() {
    return List.of();
  }

  @Override
  public List<GitPullRequestDto> getPullRequests(String repoName) {
    return List.of();
  }

  @Override
  public List<WebHooksDto> getWebHooks(String repoName) {
    return List.of();
  }

  @Override
  public GitRepoDto getRepository(String repoName) {
    return null;
  }

  @Override
  public Optional<GitWebhookDto> parseWebhook(HttpHeaders headers, String rawBody) {
    return Optional.empty();
  }


}
