package fr.uge.controller;

import fr.uge.model.Repo;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;


@Controller("/repo")
public class RepoController {

    private final  Requetes requetes;

    public RepoController(Requetes requetes){
        Objects.requireNonNull(requetes);
        this.requetes = requetes;
        super();
    }

    @Get
    public List<Repo> getAllRep() throws  SQLException{
        return  requetes.getAllRep();
    }

    @Get("/{repId}")
    public  Repo getRep(int repId) throws  SQLException{
        return requetes.getRep(repId);
    }
}
