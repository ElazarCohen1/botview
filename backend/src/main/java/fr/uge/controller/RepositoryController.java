package fr.uge.controller;

import fr.uge.database.Database;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.RepositoryDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller("/api/repositories")
@ExecuteOn(TaskExecutors.BLOCKING)
public class RepositoryController {
  private final GitProviderFactory factoryProvider;
  private final Database db;

  public RepositoryController(GitProviderFactory factoryProvider, Database database) {
    Objects.requireNonNull(factoryProvider);
    Objects.requireNonNull(database);
    this.db = database;
    this.factoryProvider = factoryProvider;
    super();
  }

  @Get("/git")
  public List<GitRepoDto> getRepositories(@QueryValue GitProviderType provider) {
    Objects.requireNonNull(provider);
    return this.factoryProvider.getProvider(provider).getRepositories();
  }

  @Get
  public List<RepositoryDto> getRegisteredRepositories(@QueryValue GitProviderType provider) throws SQLException {
    Objects.requireNonNull(provider);
    return db.getRepositoryQueries().getAllRepositories(provider);
  }


  // to modify a table
  // the + to take all the path after repositories
  @Patch("/{+repositoryId}")
  public HttpResponse<?> removeWebhook(int repositoryId, @QueryValue GitProviderType provider) {
    Objects.requireNonNull(provider);
    return factoryProvider.getProvider(provider).removeWebhookFromRepository(repositoryId);

  }


  @Post("/{+repoName}")
  public HttpResponse<?> addRepository(String repoName, @QueryValue GitProviderType provider) throws SQLException {
    Objects.requireNonNull(provider);
    try {
      factoryProvider.getProvider(provider).addRepository(repoName);
      return HttpResponse.ok();
    } catch (HttpClientResponseException e) {
      return HttpResponse.status(e.getStatus())
              .body(Map.of(
                      "error", "Impossible de créer le webhook.",
                      "message", e.getMessage()
              ));
    }
  }
}

