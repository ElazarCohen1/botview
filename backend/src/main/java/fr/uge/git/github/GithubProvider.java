package fr.uge.git.github;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import jakarta.inject.Singleton;
import java.util.Objects;

@Singleton
public class GithubProvider implements GitProvider {
  private final GithubClient client;
  private String token ;

  public GithubProvider(GithubClient client){
      Objects.requireNonNull(client);
      this.client = client;
      this.token = "";
      super();
  }

  @Override
  public GitUserDto getUser(String token){
    return client.getUser(token);
  }

  @Override
  public void saveToken(String token) {
    this.token = token;
  }

  @Override
  public String getToken() {
    return token;
  }


}
