package fr.uge.git;

import fr.uge.git.model.*;
import io.micronaut.http.HttpHeaders;
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

  CloneParams cloneSpec(GitWebhookDto event);

  String getPullRequestDiff(String repoName, long prNumber);
}
