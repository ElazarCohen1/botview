package fr.uge.database;

import fr.uge.model.PullRequest;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Singleton
public class PullRequestQueries {
  private final DataSource dataSource;

  public PullRequestQueries(DataSource dataSource) {
    Objects.requireNonNull(dataSource);
    this.dataSource = dataSource;
    super();
  }


  /**
   * Counts all pull requests stored in the database.
   *
   * @return the total number of pull requests
   * @throws SQLException if a database error occurs
   */
  @Transactional
  public int countAllPullRequests() throws SQLException {
    var sql = """
            SELECT COUNT(*) AS total
            FROM PULL_REQUEST
            """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql);
         var res = stmt.executeQuery()) {

      if (res.next()) {
        return res.getInt("total");
      }

      return 0;
    }
  }


  /**
   * Gets all the pull requests from a repository
   *
   * @param repositoryId Id of the repo we want to get all the pull request from
   * @return a list of all stored pull requests in database for the repository repositoryId
   * @throws SQLException if there is database error
   */
  @Transactional
  public List<PullRequest> getPullRequestOfRepository(int repositoryId) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      var stmt = conn.prepareStatement(" SELECT id ,number,name, url,state,created_at ,updated_at,repository_id from PULL_REQUEST WHERE repository_id = ?");
      stmt.setInt(1, repositoryId);
      var res = stmt.executeQuery();
      var pullRequests = new ArrayList<PullRequest>();
      while (res.next()) {
        pullRequests.add(new PullRequest(
                res.getInt("id"),
                res.getInt("number"),
                res.getString("name"),
                res.getString("url"),
                res.getTimestamp("created_at").toInstant(),
                res.getTimestamp("updated_at").toInstant(),
                res.getInt("repository_id"),
                res.getString("state"))

        );
      }
      return pullRequests;
    }
  }

  /**
   * Inserts a pull request into the database.
   *
   * @param pullRequestId the pull request number
   * @param url           the pull request URL
   * @param status        the current pull request status
   * @param repositoryId  the ID of the repository linked to the pull request
   * @throws SQLException if a database error occurs
   */
  @Transactional
  public void insertPullRequest(long pullRequestId, String url, String status, int repositoryId) throws SQLException {
    Objects.requireNonNull(url);
    Objects.requireNonNull(status);
    var sql = """
            INSERT INTO PULL_REQUEST (
                number,
                name,
                url,
                state,
                created_at,
                updated_at,
                repository_id
            )
            VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, ?)
            """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, pullRequestId);
      stmt.setString(2, "PR #" + pullRequestId);
      stmt.setString(3, url);
      stmt.setString(4, status.toUpperCase(Locale.ROOT));
      stmt.setInt(5, repositoryId);

      stmt.executeUpdate();
    }
  }


  /**
   * Updates the state and the last modification date of a pull request.
   *
   * @param pullRequestId the ID of the pull request to update
   * @param state         the new state of the pull request
   * @throws SQLException if a database error occurs
   */
  @Transactional
  public void updatePullRequest(long pullRequestId, int repoId, String state) throws SQLException {
    Objects.requireNonNull(state);

    var sql = """
            UPDATE PULL_REQUEST
            SET state = ?, updated_at = CURRENT_TIMESTAMP
            WHERE number = ? AND repository_id = ?
            """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, state.toUpperCase(Locale.ROOT));
      stmt.setLong(2, pullRequestId);
      stmt.setInt(3, repoId);

      stmt.executeUpdate();
      IO.println("pr updated in bdd: " + state + " " + pullRequestId + " " + repoId);
    }
  }


  /**
   * Get a specific pull request from the database
   *
   * @param prId id of the pull request by its identifier
   * @return the pull request
   * @throws SQLException if there is database error
   */
  @Transactional
  public PullRequest getPullRequest(int prId) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      var stmt = conn
              .prepareStatement("SELECT id ,number,name, url,state,created_at ,updated_at,repository_id from PULL_REQUEST where id = ?");
      stmt.setInt(1, prId);
      ResultSet res = stmt.executeQuery();
      if (!res.next()) {
        return null;
      }
      return new PullRequest(
              res.getInt("id"),
              res.getInt("number"),
              res.getString("name"),
              res.getString("url"),
              res.getTimestamp("created_at").toInstant(),
              res.getTimestamp("updated_at").toInstant(),
              res.getInt("repository_id"),
              res.getString("state")
      );
    }
  }
}
