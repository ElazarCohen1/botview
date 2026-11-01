package fr.uge.git;


import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.core.type.Argument;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface GitProvider {
  GitUserDto getUser();

  void saveToken(String token);

  String getToken();

  List<GitRepoDto> getRepositories();
  List<GitPullRequestDto> getPullRequests(String repoName);

  List<WebHooksDto> getWebHooks(String repoName);

  WebHooksDto createWebhook (String repoName);

  GitRepoDto getRepository(String repoName);

}
