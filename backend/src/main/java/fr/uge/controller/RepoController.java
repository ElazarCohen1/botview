package fr.uge.controller;

import fr.uge.model.Repository;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


@Controller("/repo")
public class RepoController {

  private final Requetes requetes;

  public RepoController(Requetes requetes) {
    Objects.requireNonNull(requetes);
    this.requetes = requetes;
    super();
  }

  @Get
  public List<Repository> getAllRep() throws SQLException {
    return requetes.getAllRep();
  }

  @Get("/{repId}")
  public Repository getRep(int repId) throws SQLException {
    return requetes.getRep(repId);
  }
}
