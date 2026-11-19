package fr.uge.database;

import fr.uge.review.ReviewStatus;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class ReviewQueries {
  private final DataSource dataSource;


  public ReviewQueries(DataSource datasource) {
    Objects.requireNonNull(datasource);
    this.dataSource = datasource;
    super();
  }

  @Transactional
  public int insertReview(String globalComment, ReviewStatus status, boolean isBlocking, int prId) throws SQLException {
    Objects.requireNonNull(globalComment);
    Objects.requireNonNull(status);
    try (var conn = dataSource.getConnection()) {
      var stmt = conn.prepareStatement("""
              INSERT INTO REVIEW (
                  global_comment,
                  status,
                  is_blocking,
                  pull_request_id
              )
              VALUES (?, ?, ?, ?)
              RETURNING id
              """);
      stmt.setString(1, globalComment);
      stmt.setString(2, status.name());
      stmt.setBoolean(3, isBlocking);
      stmt.setInt(4, prId);
      try (var rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Failed to insert review");
        }
        return rs.getInt("id");
      }
    }

  }
}

