package fr.uge.git.gitlab;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import jakarta.inject.Singleton;

import java.util.List;

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

  @Override
  public List<GitRepoDto> getRepositories(){
    return List.of();
  }
  @Override
  public List<GitPullRequestDto> getPullRequests(String repoName){
    return List.of();
  }

  @Override
  public List<WebHooksDto> getWebHooks(String repoName){
    return List.of();
  }

  @Override
  public WebHooksDto createWebhook (String repoName){
    return  null;
  }

  @Override
  public GitRepoDto getRepository(String repoName){return null;}

}
