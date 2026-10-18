package fr.uge.controller;
import java.util.List;
import java.util.Objects;

import fr.uge.git.GitProvider;
import fr.uge.git.github.GithubClient;
import fr.uge.git.github.GithubProvider;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api/hooks")
@ExecuteOn(TaskExecutors.BLOCKING)
public class WebHooksController {
    private final GithubClient githubClient;
    private  final GitProvider githubProvider;

    public WebHooksController(GithubClient client, GithubProvider provider) {
        Objects.requireNonNull(client);
        this.githubClient = client;
        this.githubProvider = provider;
        super();
    }

    @Post("/{repo}")
    public HttpResponse<?> createWebHook(String repo){
        Objects.requireNonNull(repo);
        String token = githubProvider.getToken();
        githubClient.createWebhook(repo,token);
        return  HttpResponse.ok( );
    }

    @Get("/{repo}")
    public List<WebHooksDto> WebHooks(String repo){
        Objects.requireNonNull(repo);
        return  githubClient.getWebHooks(repo);
}
    @Post
    public  HttpResponse<?> receiveFromGitHubWebHooks(){
        System.out.println("Pull_request");
        return  HttpResponse.ok();
    }
}
