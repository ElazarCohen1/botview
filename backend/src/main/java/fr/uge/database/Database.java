package fr.uge.database;

import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class Database {
  private final PullRequestQueries pullRequestQueries;
  private final RepositoryQueries repositoryQueries;
  private final ReviewQueries reviewQueries;
  public Database(PullRequestQueries prQueries, RepositoryQueries repoQueries,ReviewQueries reviewQueries) {
    Objects.requireNonNull(prQueries);
    Objects.requireNonNull(repoQueries);
    this.pullRequestQueries = prQueries;
    this.repositoryQueries = repoQueries;
    this.reviewQueries = reviewQueries;
    super();
  }

  public PullRequestQueries getPullRequestQueries() {
    return pullRequestQueries;
  }

  public RepositoryQueries getRepositoryQueries() {
    return repositoryQueries;
  }

  public ReviewQueries getReviewQueries(){return reviewQueries;}
}
