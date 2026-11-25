
package fr.uge.database;

import fr.uge.review.model.ChangeStatus;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class ModifiedLineQueries {
  private final DataSource dataSource;

  public ModifiedLineQueries(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource);
  }

  @Transactional
  public int insert(long fileId, int lineNumber, ChangeStatus status,
                    String oldValue, String newValue) throws SQLException {

    Objects.requireNonNull(status);

    var sql = """
        INSERT INTO MODIFIED_LINE (
            file_id,
            line_number,
            status,
            old_value,
            new_value
        )
        VALUES (?, ?, ?, ?, ?)
        RETURNING id
        """;

    try (var conn = dataSource.getConnection();
         var stmt = conn.prepareStatement(sql)) {

      stmt.setLong(1, fileId);
      stmt.setInt(2, lineNumber);
      stmt.setString(3, status.name());
      stmt.setString(4, oldValue);
      stmt.setString(5, newValue);

      try (var rs = stmt.executeQuery()) {
        if (!rs.next()) {
          throw new SQLException("Failed to insert modified line");
        }
        return rs.getInt("id");
      }
    }
  }
}
