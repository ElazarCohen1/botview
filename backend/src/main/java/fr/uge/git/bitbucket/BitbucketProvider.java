package fr.uge.git.bitbucket;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;

public class BitbucketProvider implements GitProvider {
  public GitUserDto getUser(String token){
    return new GitUserDto("",0,"null");
  }

}
