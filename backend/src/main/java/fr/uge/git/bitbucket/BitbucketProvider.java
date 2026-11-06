package fr.uge.git.bitbucket;

import fr.uge.git.GitProvider;
import fr.uge.git.model.*;
import io.micronaut.http.HttpHeaders;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class BitbucketProvider implements GitProvider {

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

  @Override
  public Optional<GitWebhookDto> parseWebhook(HttpHeaders headers, String rawBody) {
    return Optional.empty();
  }

}
