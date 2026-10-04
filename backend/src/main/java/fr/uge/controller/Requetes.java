package fr.uge.controller;

import fr.uge.enums.GitProvider;
import fr.uge.model.PullRequest;
import fr.uge.model.Repository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import javax.sql.DataSource;
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
  public List<Repository> getAllRep() throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      Statement stmt = conn.createStatement();
      ResultSet res = stmt.executeQuery("SELECT rep_id, nom, url, plateforme, branche from REPO_GIT");
      List<Repository> all_rep = new ArrayList<>();
      while (res.next()) {
        all_rep.add(new Repository(res.getInt("rep_id"), res.getString("nom"), res.getString("url"), GitProvider.valueOf(res.getString("plateforme")), res.getString("branche")));
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
  public Repository getRep(int repId) throws SQLException {
    try (Connection conn = dataSource.getConnection()) {
      PreparedStatement stmt = conn.prepareStatement("SELECT rep_id, nom, url, plateforme, branche from REPO_GIT where rep_id = ?");
      stmt.setInt(1, repId);
      ResultSet res = stmt.executeQuery();
      if (!res.next()) {
        return null;
      }
      return new Repository(res.getInt("rep_id"), res.getString("nom"), res.getString("url"), GitProvider.valueOf(res.getString("plateforme")), res.getString("branche"));
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
// changer les nom et mettre expli
// java doc