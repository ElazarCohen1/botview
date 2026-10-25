package fr.uge.controller;

import fr.uge.database.Database;
import fr.uge.git.GitProvider;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.RepositoryDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller("/api/repositories")
@ExecuteOn(TaskExecutors.BLOCKING)
public class RepoController {
    private final GitProviderFactory factoryProvider;
    private final Database db;
    public RepoController(GitProviderFactory factoryProvider, Database database) {
        Objects.requireNonNull(factoryProvider);
        Objects.requireNonNull(database);
        this.db = database;
        this.factoryProvider = factoryProvider;
        super();
    }

    @Get
    public List<GitRepoDto> getRepos(@QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        return this.factoryProvider.getProvider(provider).getRepos();
    }

    @Get("/database")
    public List<RepositoryDto> getAllReposFromDb(@QueryValue GitProviderType provider) throws SQLException {
        Objects.requireNonNull(provider);
        return  db.getRepositoryQueries().getAllRep(provider);
    }

    @Post("/database/addrepo/{repo}")
    public HttpResponse<?> addRepoToDataBase(String repo,@QueryValue GitProviderType provider) throws SQLException{
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        GitRepoDto repository = factoryProvider.getProvider(provider).getOneRepos(repo);
        db.getRepositoryQueries().addRepo(repository.name(),repository.htmlUrl(),provider);
        return HttpResponse.ok();
    }
}
