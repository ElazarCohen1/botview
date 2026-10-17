package fr.uge.controller;

import fr.uge.git.GitProvider;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.bitbucket.BitbucketProvider;
import fr.uge.git.github.GithubProvider;
import fr.uge.git.gitlab.GitlabProvider;
import fr.uge.model.TokenRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.Objects;

@Controller("/api/token")
public class TokenController {

  private final GitProviderFactory factory;

  public TokenController(
          GitProviderFactory factory) {

    this.factory = factory;

  }
  @Post
  public HttpResponse<?> saveToken(@Body TokenRequest request) {
    Objects.requireNonNull(request);
    if (request.token() == null || request.token().isBlank()) {
      return HttpResponse.badRequest();
    }

    GitProvider provider = factory.getProvider(request.provider());
    provider.saveToken(request.token());

    return HttpResponse.noContent();
  }

}
