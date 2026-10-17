package fr.uge.git;

import fr.uge.git.bitbucket.BitbucketProvider;
import fr.uge.git.github.GithubProvider;
import fr.uge.git.gitlab.GitlabProvider;
import jakarta.inject.Singleton;
import java.util.Map;
import java.util.Objects;

@Singleton
public class GitProviderFactory {

  private final Map<GitProviderType, GitProvider> providers;

  public GitProviderFactory(
          GithubProvider github,
          GitlabProvider gitlab,
          BitbucketProvider bitbucket) {

    this.providers = Map.of(
            GitProviderType.GITHUB, github,
            GitProviderType.GITLAB, gitlab,
            GitProviderType.BITBUCKET, bitbucket
    );
  }

  public GitProvider getProvider(GitProviderType type) {
    Objects.requireNonNull(type);

    var provider = providers.get(type);
    if (provider == null) {
      throw new IllegalArgumentException("Provider inconnu : " + type);
    }

    return provider;
  }
}