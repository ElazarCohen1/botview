package fr.uge.git.gitlab;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import jakarta.inject.Singleton;

@Singleton
public class GitlabProvider implements GitProvider {
  private String token ;
  
  @Override
  public GitUserDto getUser(){
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
