package fr.uge.git;


import fr.uge.git.model.*;
import io.micronaut.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

public interface GitProvider {
  GitUserDto getUser();

  void saveToken(String token);

  String getToken();

  List<GitRepoDto> getRepositories();
  List<GitPullRequestDto> getPullRequests(String repoName);

  List<WebHooksDto> getWebHooks(String repoName);

  WebHooksDto createWebhook (String repoName);

  GitRepoDto getRepository(String repoName);

  Optional<GitWebhookDto> parseWebhook(HttpHeaders headers, String rawBody);
}
