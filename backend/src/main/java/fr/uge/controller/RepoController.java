package fr.uge.controller;

import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.github.GithubClient;
import fr.uge.git.model.GitRepoDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.Objects;

@Controller("/api/repositories")
@ExecuteOn(TaskExecutors.BLOCKING)
public class RepoController {
    private final GitProviderFactory Client;

    public RepoController(GitProviderFactory client) {
        Objects.requireNonNull(client);
        this.Client = client;
        super();
    }

    @Get
    public List<GitRepoDto> Repos(@QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        return this.Client.getProvider(provider).getRepos();
    }

}
