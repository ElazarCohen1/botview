
package fr.uge.database;

import fr.uge.review.model.ChangeStatus;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class FileQueries {
  private final DataSource dataSource;

  public FileQueries(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource);
  }

  @Transactional
  public int insert(String name, String relativePath, ChangeStatus status)
          throws SQLException {

    Objects.requireNonNull(name);
    Objects.requireNonNull(relativePath);
    Objects.requireNonNull(status);

    var sql = """
        INSERT INTO FILE (name, relative_path, status)
        VALUES (?, ?, ?)
        RETURNING id
        """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setString(1, name);
      stmt.setString(2, relativePath);
      stmt.setString(3, status.name());

      try (var rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Failed to insert file");
        }
        return rs.getInt("id");
      }
    }
  }

  @Transactional
  public void associateWithPullRequest(long pullRequestId, long fileId)
          throws SQLException {

    var sql = """
        INSERT INTO MODIFIES (pull_request_id, file_id)
        VALUES (?, ?)
        """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, pullRequestId);
      stmt.setLong(2, fileId);

      stmt.executeUpdate();
    }
  }
}
