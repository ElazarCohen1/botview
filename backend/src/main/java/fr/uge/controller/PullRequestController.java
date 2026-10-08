package fr.uge.controller;

import fr.uge.git.github.GithubClient;
import fr.uge.git.model.GitPullRequestDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.util.List;
import java.util.Objects;

@Controller("/api/pullRequest")
@ExecuteOn( TaskExecutors.BLOCKING)
public class PullRequestController<Request> {
  private final GithubClient githubClient;

  public PullRequestController(GithubClient githubClient) {
    this.githubClient = githubClient;
    super();
  }

  @Get("/{repo}")
  public List<GitPullRequestDto> pullRequests(String repo){
    return githubClient.getPullRequests(repo,null);
  }

}
