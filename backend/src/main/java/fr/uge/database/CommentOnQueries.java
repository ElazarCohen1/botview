
package fr.uge.database;

import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class CommentOnQueries {
  private final DataSource dataSource;

  public CommentOnQueries(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource);
  }

  @Transactional
  public int insert(long reviewId, long modifiedLineId,
                    boolean humanFlag, String message) throws SQLException {
    Objects.requireNonNull(message);

    var sql = """
        INSERT INTO COMMENTS_ON (
            review_id,
            modified_line_id,
            human_flag,
            message
        )
        VALUES (?, ?, ?, ?)
        RETURNING id
        """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, reviewId);
      stmt.setLong(2, modifiedLineId);
      stmt.setBoolean(3, humanFlag);
      stmt.setString(4, message);

      try (var rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Failed to insert comment");
        }
        return rs.getInt("id");
      }
    }
  }
}
