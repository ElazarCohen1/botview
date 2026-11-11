package fr.uge.controller;

import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.WebHooksDto;
import fr.uge.review.ReviewQueue;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller("/api/hooks")
@ExecuteOn(TaskExecutors.BLOCKING)
public class WebHooksController {
  private final GitProviderFactory factoryProvider;
  private final ReviewQueue queue;

  public WebHooksController(GitProviderFactory factoryProvider, ReviewQueue queue) {
    Objects.requireNonNull(factoryProvider);
    this.factoryProvider = factoryProvider;
    this.queue = queue;
    super();
  }


  @Get("/{+repo}")
  public List<WebHooksDto> getWebHooks(String repo, @QueryValue GitProviderType provider) {
    Objects.requireNonNull(provider);
    Objects.requireNonNull(repo);
    return factoryProvider.getProvider(provider).getWebHooks(repo);
  }

  @Post("/receive/{provider}")
  public HttpResponse<?> receiveWebHooks(
          @PathVariable GitProviderType provider,
          @Body String rawBody,
          HttpHeaders headers
  ) {
    var git = factoryProvider.getProvider(provider);
    // add the webhook to the list for the review
    try {
      git.parseWebhook(headers, rawBody)
              // for doing a reviews only on opened and push on the pull request not the closed etc
              .filter(e -> Set.of("opened", "synchronize", "reopened").contains(e.action()))
              .ifPresent(queue::submit);

    } catch (IllegalArgumentException e) {
      return HttpResponse.badRequest();
    }
    return HttpResponse.accepted();
  }
}
