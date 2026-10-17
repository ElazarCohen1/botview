package fr.uge.git.bitbucket;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import jakarta.inject.Singleton;

@Singleton
public class BitbucketProvider implements GitProvider {

  private String token ;

  @Override
  public GitUserDto getUser(String token){
    return new GitUserDto("",0,"null");
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
