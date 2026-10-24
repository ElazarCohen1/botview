package fr.uge.controller;

import fr.uge.git.GitProviderType;
import fr.uge.model.PullRequest;
import fr.uge.git.model.RepositoryDto;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton
public class Requetes {
  private final DataSource dataSource;

  public Requetes(DataSource dataSource) {
    Objects.requireNonNull(dataSource);
    this.dataSource = dataSource;
    super();
  }

  @Transactional
  public List<RepositoryDto> getAllRep(GitProviderType provider) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("SELECT id, name, url, origin from REPOSITORY where origin = ?");
      stmt.setString(1, provider.name());
      List<RepositoryDto> all_rep = new ArrayList<>();
      ResultSet res = stmt.executeQuery();
      while (res.next()) {
        all_rep.add(new RepositoryDto(res.getInt("id"), res.getString("name"), res.getString("url"), GitProviderType.valueOf(res.getString("origin"))));
      }
      return all_rep;
    }
  }

  @Transactional
  public List<PullRequest> getAllPr() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      Statement stmt = conn.createStatement();
      ResultSet res = stmt.executeQuery("SELECT pull_id, commentaire, name, date from PULL_REQUEST");
      List<PullRequest> all_rep = new ArrayList<>();
      while (res.next()) {
        all_rep.add(new PullRequest(res.getInt("pull_id"), res.getString("commentaire"), res.getString("name"), res.getTimestamp("date").toInstant()));
      }
      return all_rep;
    }
  }

  @Transactional
  public RepositoryDto getRep(int repId) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("SELECT id, name, url, origin from REPOSITORY where id = ?");
      stmt.setInt(1, repId);
      ResultSet res = stmt.executeQuery();
      if (!res.next()) {
        return null;
      }
      return new RepositoryDto(res.getInt("id"), res.getString("name"), res.getString("url"), GitProviderType.valueOf(res.getString("origin")));
    }
  }

  @Transactional
  public void addRepo(String name, String url, GitProviderType provider) throws SQLException{
    try (Connection conn = dataSource.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO REPOSITORY (name,url,origin) VALUES ( ?, ?, ? )");
      stmt.setString(1, name);
      stmt.setString(2, url);
      stmt.setString(3, provider.name());
      stmt.execute();
      }
  }

  @Transactional
  public PullRequest getPr(int prId) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("SELECT pull_id, commentaire, name, date from PULL_REQUEST where pull_id = ?");
      stmt.setInt(1, prId);
      ResultSet res = stmt.executeQuery();
      if (!res.next()) {
        return null;
      }
      return new PullRequest(res.getInt("pull_id"), res.getString("commentaire"), res.getString("name"), res.getTimestamp("date").toInstant());
    }
  }
}
