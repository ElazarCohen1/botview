package fr.uge.controller;

import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.github.GithubClient;
import fr.uge.git.model.GitPullRequestDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
import java.util.Objects;

@Controller("/api/pullRequest")
@ExecuteOn(TaskExecutors.BLOCKING)
public class PullRequestController<Request> {
  private final GitProviderFactory Client;

  public PullRequestController(GitProviderFactory Client) {
    this.Client = Client;
    super();
  }

  @Get("/{repo}")
  public List<GitPullRequestDto> pullRequests(String repo, @QueryValue GitProviderType provider) {
    Objects.requireNonNull(provider);
    Objects.requireNonNull(repo);
    return Client.getProvider(provider).getPullRequests(repo);
  }

}
