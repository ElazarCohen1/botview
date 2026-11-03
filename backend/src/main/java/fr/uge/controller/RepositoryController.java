package fr.uge.controller;

import fr.uge.database.Database;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.RepositoryDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Controller("/api/repositories")
@ExecuteOn(TaskExecutors.BLOCKING)
public class RepositoryController {
    private final GitProviderFactory factoryProvider;
    private final Database db;

    public RepositoryController(GitProviderFactory factoryProvider, Database database) {
        Objects.requireNonNull(factoryProvider);
        Objects.requireNonNull(database);
        this.db = database;
        this.factoryProvider = factoryProvider;
        super();
    }

    @Get
    public List<GitRepoDto> getRepositories(@QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        return this.factoryProvider.getProvider(provider).getRepositories();
    }

    @Get("/registered")
    public List<RepositoryDto> getRegisteredRepositories(@QueryValue GitProviderType provider) throws SQLException {
        Objects.requireNonNull(provider);
        return db.getRepositoryQueries().getAllRepositories(provider);
    }

    @Delete("/{+repo}")
    public HttpResponse<?> deleteRepository(String repo, @QueryValue GitProviderType provider) throws SQLException {
        Objects.requireNonNull(repo);
        Objects.requireNonNull(provider);
        db.getRepositoryQueries().deleteRepository(repo, provider);
        return HttpResponse.ok();
    }

    @Post("/{+repo}")
    public HttpResponse<?> addRepository(String repo, @QueryValue GitProviderType provider) throws SQLException {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        GitRepoDto repository = factoryProvider.getProvider(provider).getRepository(repo);
        db.getRepositoryQueries().addRepository(repository.fullName(), repository.htmlUrl(), provider);
        return HttpResponse.ok();
    }
}
