package fr.uge;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;


// one only instance of this class
@Singleton
public class DatabaseInitializer {

  private final DataSource dataSource;

  public DatabaseInitializer(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource);
  }

  @Connectable
  @EventListener // run this method on the start of the application
  public void onStartup(StartupEvent event) {
    try {
      init();
    } catch (SQLException e) {
      System.err.println("The initialisation of the database crashed !!" + e);
      System.exit(1);
    }
  }

  public void init() throws SQLException{
    try (var conn = dataSource.getConnection();
         var stmt = conn.createStatement()) {

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS REPOSITORY (
          id INTEGER PRIMARY KEY,
          name VARCHAR,
          url VARCHAR,
          origin VARCHAR
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS PULL_REQUEST (
          id INTEGER PRIMARY KEY,
          name VARCHAR,
          commit_id VARCHAR,
          status VARCHAR,
          created_at TIMESTAMP,
          repository_id INTEGER REFERENCES REPOSITORY(id)
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS REVIEW (
          id INTEGER PRIMARY KEY,
          global_comment TEXT,
          status VARCHAR,
          is_blocking BOOLEAN,
          date TIMESTAMP,
          pull_request_id INTEGER REFERENCES PULL_REQUEST(id)
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS ANALYSIS (
          id INTEGER PRIMARY KEY,
          tool VARCHAR,
          result TEXT,
          status VARCHAR,
          date TIMESTAMP,
          review_id INTEGER REFERENCES REVIEW(id)
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS FILE (
          id INTEGER PRIMARY KEY,
          status VARCHAR,
          name VARCHAR,
          relative_path VARCHAR,
          date TIMESTAMP
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS MODIFIED_LINE (
          id INTEGER PRIMARY KEY,
          status VARCHAR,
          old_value TEXT,
          new_value TEXT,
          file_id INTEGER REFERENCES FILE(id)
        )""");

      // Associations
      stmt.execute("""
        CREATE TABLE IF NOT EXISTS MODIFIES (
          pull_request_id INTEGER REFERENCES PULL_REQUEST(id),
          file_id INTEGER REFERENCES FILE(id),
          PRIMARY KEY (pull_request_id, file_id)
        )""");

      stmt.execute("""
        CREATE TABLE IF NOT EXISTS COMMENTS_ON (
          review_id INTEGER REFERENCES REVIEW(id),
          modified_line_id INTEGER REFERENCES MODIFIED_LINE(id),
          human_flag BOOLEAN,
          PRIMARY KEY (review_id, modified_line_id)
        )""");

    }
  }
}