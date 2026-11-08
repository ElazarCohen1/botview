package fr.uge.database;

import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class Database {
  private final PullRequestQueries pullRequestQueries;
  private final RepositoryQueries repositoryQueries;

  public Database(PullRequestQueries prQueries, RepositoryQueries repoQueries) {
    Objects.requireNonNull(prQueries);
    Objects.requireNonNull(repoQueries);
    this.pullRequestQueries = prQueries;
    this.repositoryQueries = repoQueries;
    super();
  }

  public PullRequestQueries getPullRequestQueries() {
    return pullRequestQueries;
  }

  public RepositoryQueries getRepositoryQueries() {
    return repositoryQueries;
  }

}
