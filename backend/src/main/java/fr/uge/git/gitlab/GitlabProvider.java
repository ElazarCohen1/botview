package fr.uge.git.gitlab;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpResponse;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Objects;

@Singleton
public class GitlabProvider implements GitProvider {
  private final String token;

  public GitlabProvider(@Value("${gitlab.token:}") String token) {
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

}
