package fr.uge.git;


import fr.uge.git.model.GitPullRequestDto;
import fr.uge.git.model.GitRepoDto;
import fr.uge.git.model.GitUserDto;
import fr.uge.git.model.WebHooksDto;
import io.micronaut.http.HttpResponse;

import java.util.List;
import java.util.Optional;

public interface GitProvider {
  GitUserDto getUser();

  String getToken();

  void addRepository(String repoName);

  HttpResponse<?> removeWebhookFromRepository(int repoId);

  List<GitRepoDto> getRepositories();

  List<GitPullRequestDto> getPullRequests(String repoName);

  List<WebHooksDto> getWebHooks(String repoName);

  GitRepoDto getRepository(String repoName);

  Optional<GitWebhookDto> parseWebhook(HttpHeaders headers, String rawBody);
}
