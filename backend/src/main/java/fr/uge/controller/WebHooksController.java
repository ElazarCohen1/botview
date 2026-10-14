package fr.uge.controller;
import java.util.List;
import java.util.Objects;
import fr.uge.git.github.GithubClient;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api/hooks")
@ExecuteOn(TaskExecutors.BLOCKING)
public class WebHooksController {
    private final GithubClient githubClient;

    public WebHooksController(GithubClient client) {
        Objects.requireNonNull(client);
        this.githubClient = client;
        super();
    }

    @Get("/{repo}")
    public List<WebHooksDto> WebHooks(String repo){
        Objects.requireNonNull(repo);
        return  githubClient.getWebHooks(repo);
}

}
