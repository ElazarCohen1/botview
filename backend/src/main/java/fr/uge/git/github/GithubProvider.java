package fr.uge.git.github;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import jakarta.inject.Singleton;
import java.util.Objects;

@Singleton
public class GithubProvider implements GitProvider {
  private final GithubClient client;

  public GithubProvider(GithubClient client){
      Objects.requireNonNull(client);
      this.client = client;
  }
  public GitUserDto getUser(String token){
    return client.getUser(token);
  }


}
