package fr.uge.controller;

import java.util.List;
import java.util.Objects;

import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api/hooks")
@ExecuteOn(TaskExecutors.BLOCKING)
public class WebHooksController {
    private final GitProviderFactory factoryProvider;

    public WebHooksController(GitProviderFactory factoryProvider) {
        Objects.requireNonNull(factoryProvider);
        this.factoryProvider = factoryProvider;
        super();
    }

    @Post("/{+repo}")
    public HttpResponse<?> createWebHook(String repo, @QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        try {
            factoryProvider.getProvider(provider).createWebhook(repo);
            return HttpResponse.ok();
        } catch (HttpClientResponseException e) {
            return HttpResponse.badRequest(String.valueOf(e.getMessage()) );
        }

    }

    @Get("/{+repo}")
    public List<WebHooksDto> getWebHooks(String repo, @QueryValue GitProviderType provider) {
        Objects.requireNonNull(provider);
        Objects.requireNonNull(repo);
        return factoryProvider.getProvider(provider).getWebHooks(repo);
    }

    @Post
    public HttpResponse<?> receiveFromGitHubWebHooks() {
        IO.println("Pull_request");
        return HttpResponse.ok();
    }

}
