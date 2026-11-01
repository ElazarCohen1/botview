package fr.uge.controller;

import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.GitPullRequestDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.Objects;

@Controller("/api/pullrequest")
@ExecuteOn(TaskExecutors.BLOCKING)
public class PullRequestController<Request> {
  private final GitProviderFactory factoryProvider;

  public PullRequestController(GitProviderFactory factoryProvider) {
    Objects.requireNonNull(factoryProvider);
    this.factoryProvider = factoryProvider;
    super();
  }

  @Get("/{repo}")
  public List<GitPullRequestDto> getPullRequests(String repo, @QueryValue GitProviderType provider) {
    Objects.requireNonNull(provider);
    Objects.requireNonNull(repo);
    return factoryProvider.getProvider(provider).getPullRequests(repo);
  }

}
