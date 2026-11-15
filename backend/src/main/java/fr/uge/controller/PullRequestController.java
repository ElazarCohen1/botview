package fr.uge.controller;

import fr.uge.database.Database;
import fr.uge.model.PullRequest;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Controller("/api/pullrequest")
@ExecuteOn(TaskExecutors.BLOCKING)
public class PullRequestController<Request> {
  private final Database database;

  public PullRequestController(Database database) {
    this.database = database;
    super();
  }

  @Get("/{repositoryId}")
  public List<PullRequest> getPullRequests(int repositoryId) {
    try {
      return database.getPullRequestQueries().getPullRequestOfRepository(repositoryId);
    } catch (SQLException e) {
      return Collections.emptyList();
    }
  }


  @Get("/count")
  public int countAllPullRequest() {
    try {
      return database.getPullRequestQueries().countAllPullRequests();

    } catch (SQLException e) {
      return 0;
    }
  }

}