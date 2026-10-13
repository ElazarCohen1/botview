package fr.uge.git.github;

import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;

import java.util.List;
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
    Objects.requireNonNull(path);
    Objects.requireNonNull(token);

    return HttpRequest.GET(path)
            .header("Authorization", "Bearer " + token)
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "pr-review-bot");
  }

  public GitUserDto getUser(){
    return client.retrieve(get("/user",defaultToken),GitUserDto.class);
  }

  public List<GitRepoDto> getRepos(){
    return  client.retrieve(get("/user/repos", defaultToken), Argument.listOf(GitRepoDto.class) );
  }

  public  List<GitPullRequestDto> getPullRequests(String repoName){
    Objects.requireNonNull(repoName);
    String owner = getUser().login();
    String path = "/repos/" + owner + "/" + repoName + "/pulls";
    return  client.retrieve(get(path, defaultToken), Argument.listOf(GitPullRequestDto.class) );
  }
}
