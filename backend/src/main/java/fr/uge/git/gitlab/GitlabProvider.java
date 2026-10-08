package fr.uge.git.gitlab;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;

public class GitlabProvider implements GitProvider {
  @Override
  public GitUserDto getUser(String token){
    return new GitUserDto("",0,"null");
  }
}
