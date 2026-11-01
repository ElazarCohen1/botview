package fr.uge.git.github;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Objects;

@Singleton
public class GithubProvider implements GitProvider {
  private final GithubClient client;
  private String token;

  public GithubProvider(GithubClient client) {
    Objects.requireNonNull(client);
    this.client = client;
    this.token = "";
    super();
  }

  @Override
  public GitUserDto getUser() {
    return client.getUser(this.token);
  }

  @Override
  public void saveToken(String token) {
    this.token = token;
  }

  @Override
  public String getToken() {
    return token;
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
  public WebHooksDto createWebhook(String repoName) {
    Objects.requireNonNull(repoName);
    return client.createWebhook(repoName, this.token);
  }

  @Override
  public GitRepoDto getRepository(String repoName) {
    Objects.requireNonNull(repoName);
    return client.getRepository(repoName, this.token);
  }

}
