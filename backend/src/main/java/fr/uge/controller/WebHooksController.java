package fr.uge.controller;

import java.util.List;
import java.util.Objects;

import fr.uge.git.GitProvider;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.github.GithubClient;
import fr.uge.git.github.GithubProvider;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api/hooks")
@ExecuteOn(TaskExecutors.BLOCKING)
public class WebHooksController {
    private final GitProviderFactory Client;

    public WebHooksController(GitProviderFactory client) {
        Objects.requireNonNull(client);
        this.Client = client;
        super();
    }

    @Post("/{repo}")
    public HttpResponse<?> createWebHook(String repo, @QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        String token = Client.getProvider(provider).getToken();
        Client.getProvider(provider).createWebhook(repo);
        return HttpResponse.ok();
    }

    @Get("/{repo}")
    public List<WebHooksDto> WebHooks(String repo, @QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        return Client.getProvider(provider).getWebHooks(repo);
    }

    @Post
    public HttpResponse<?> receiveFromGitHubWebHooks() {
        System.out.println("Pull_request");
        return HttpResponse.ok();
    }
}
