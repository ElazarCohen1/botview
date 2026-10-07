package fr.uge.git.github;

import fr.uge.git.model.GitUserDto;
import io.micronaut.context.annotation.Value;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class GithubClient {
  private final BlockingHttpClient client;
  private final String defaultToken;

  public GithubClient(@Client("https://api.github.com") HttpClient client,@Value("${github.token}") String defaultToken
  ){
    Objects.requireNonNull(client);
    Objects.requireNonNull(defaultToken);

    this.client = client.toBlocking();
    this.defaultToken = defaultToken;
    super();
  }

  private HttpRequest<?> get(String path,String token) {
    return HttpRequest.GET(path)
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "pr-review-bot");
  }

  public GitUserDto getUser(String token){
    String tokenToUse = (token == null || token.isBlank())  ? defaultToken : token;
    return client.retrieve(get("/user",tokenToUse),GitUserDto.class);
  }
}
