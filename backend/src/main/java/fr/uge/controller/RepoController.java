package fr.uge.controller;

import fr.uge.git.github.GithubClient;
import fr.uge.git.model.GitRepoDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.Objects;


@Controller("/api/repositories")
@ExecuteOn( TaskExecutors.BLOCKING)
public class RepoController {
    private final GithubClient githubClient;
    public RepoController(GithubClient client){
        Objects.requireNonNull(client);
        githubClient = client;
        super();
    }

    @Get
    public List<GitRepoDto> Repos(){
        return  githubClient.getRepos(null);
    }

}
