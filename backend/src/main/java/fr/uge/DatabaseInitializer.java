package fr.uge;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.data.connection.annotation.Connectable;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

@Singleton
public class DatabaseInitializer {

  private final DataSource dataSource;

  public DatabaseInitializer(DataSource dataSource) {
    this.dataSource = Objects.requireNonNull(dataSource);
  }

  @Connectable
  @EventListener
  public void onStartup(StartupEvent event) {
    try {
      init();
    } catch (SQLException e) {
      throw new IllegalStateException("Database initialisation failed", e);
    }
  }

  public void init() throws SQLException {
    try (var conn = dataSource.getConnection();
         var stmt = conn.createStatement()) {

      createEnums(conn, stmt);
      createSequences(stmt);
      createTables(stmt);
    }
  }


  private void createEnums(Connection conn, Statement stmt) throws SQLException {
    createEnumIfAbsent(conn, stmt, "platform", "'GITHUB', 'GITLAB', 'BITBUCKET'");
    createEnumIfAbsent(conn, stmt, "pr_status", "'OPEN', 'MERGED', 'CLOSED'");
    createEnumIfAbsent(conn, stmt, "review_status", "'PENDING', 'RUNNING', 'DONE', 'FAILED'");
    createEnumIfAbsent(conn, stmt, "analysis_status", "'PENDING', 'RUNNING', 'DONE', 'FAILED'");
    createEnumIfAbsent(conn, stmt, "change_status", "'ADDED', 'MODIFIED', 'DELETED', 'RENAMED'");
  }

  private void createEnumIfAbsent(Connection conn, Statement stmt, String name, String values)
          throws SQLException {
    try (var ps = conn.prepareStatement(
            "SELECT 1 FROM duckdb_types() WHERE type_name = ?")) {
      ps.setString(1, name);
      try (var rs = ps.executeQuery()) {
        if (rs.next()) {
          return;
        }
      }
    }
    stmt.execute("CREATE TYPE " + name + " AS ENUM (" + values + ")");
  }


  private void createSequences(Statement stmt) throws SQLException {
    for (var table : new String[]{
            "repository", "pull_request", "review", "analysis", "file", "modified_line"}) {
      stmt.execute("CREATE SEQUENCE IF NOT EXISTS " + table + "_seq START 1");
    }
  }


  private void createTables(Statement stmt) throws SQLException {
    stmt.execute("""
      CREATE TABLE IF NOT EXISTS REPOSITORY (
        id          INTEGER PRIMARY KEY DEFAULT nextval('repository_seq'),
        name        VARCHAR(255) NOT NULL CHECK (length(name) <= 255),
        url         VARCHAR(2048) NOT NULL UNIQUE CHECK (length(url) <= 2048),
        origin      platform NOT NULL
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS PULL_REQUEST (
        id             INTEGER PRIMARY KEY DEFAULT nextval('pull_request_seq'),
        name           VARCHAR(255) NOT NULL,
        commit_id      VARCHAR(64) NOT NULL,       -- SHA-1 = 40 car., SHA-256 = 64
        status         pr_status NOT NULL,
        created_at     TIMESTAMP NOT NULL DEFAULT current_timestamp,
        repository_id  INTEGER NOT NULL REFERENCES REPOSITORY(id)
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS REVIEW (
        id              INTEGER PRIMARY KEY DEFAULT nextval('review_seq'),
        global_comment  TEXT,
        status          review_status NOT NULL,
        is_blocking     BOOLEAN NOT NULL DEFAULT false,
        created_at      TIMESTAMP NOT NULL DEFAULT current_timestamp,
        pull_request_id INTEGER NOT NULL REFERENCES PULL_REQUEST(id)
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS ANALYSIS (
        id          INTEGER PRIMARY KEY DEFAULT nextval('analysis_seq'),
        tool        VARCHAR(100) NOT NULL,         -- ex : checkstyle, spotbugs, llm
        result      TEXT,
        status      analysis_status NOT NULL,
        created_at  TIMESTAMP NOT NULL DEFAULT current_timestamp,
        review_id   INTEGER NOT NULL REFERENCES REVIEW(id)
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS FILE (
        id             INTEGER PRIMARY KEY DEFAULT nextval('file_seq'),
        status         change_status NOT NULL,
        name           VARCHAR(255) NOT NULL,
        relative_path  VARCHAR(1024) NOT NULL,
        created_at     TIMESTAMP NOT NULL DEFAULT current_timestamp
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS MODIFIED_LINE (
        id         INTEGER PRIMARY KEY DEFAULT nextval('modified_line_seq'),
        status     change_status NOT NULL,
        old_value  TEXT,
        new_value  TEXT,
        file_id    INTEGER NOT NULL REFERENCES FILE(id)
      )""");

    // Associations
    stmt.execute("""
      CREATE TABLE IF NOT EXISTS MODIFIES (
        pull_request_id INTEGER NOT NULL REFERENCES PULL_REQUEST(id),
        file_id         INTEGER NOT NULL REFERENCES FILE(id),
        PRIMARY KEY (pull_request_id, file_id)
      )""");

    stmt.execute("""
      CREATE TABLE IF NOT EXISTS COMMENTS_ON (
        review_id         INTEGER NOT NULL REFERENCES REVIEW(id),
        modified_line_id  INTEGER NOT NULL REFERENCES MODIFIED_LINE(id),
        human_flag        BOOLEAN NOT NULL DEFAULT false,
        PRIMARY KEY (review_id, modified_line_id)
      )""");
  }
}