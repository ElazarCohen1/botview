package fr.uge.git.bitbucket;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;

public class BitbucketProvider implements GitProvider {
  @Override
  public GitUserDto getUser(String token){
    // null valeur for default
    return new GitUserDto("",0,"null");
  }

}
