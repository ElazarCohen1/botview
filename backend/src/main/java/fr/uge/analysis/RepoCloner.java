package fr.uge.analysis;

import fr.uge.git.model.CloneParams;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Objects;


@Singleton
public class RepoCloner {

  @Value("${app.workspace.dir:./data/workspaces}")
  String workspaceDir;

  private void fetchPr(Git git, CredentialsProvider credential, String ref) throws GitAPIException {
    git.fetch()
            .setRemote("origin")
            .setCredentialsProvider(credential)
            .setRefSpecs(
                    "+" + ref + ":refs/remotes/origin/pr"
            )
            .call();
  }

  private void checkout(Git git) throws GitAPIException {
    git.checkout()
            .setCreateBranch(true)
            .setName("pr")
            .setStartPoint("refs/remotes/origin/pr")
            .call();
  }

  public Path clone(CloneParams params, String fileName) throws Exception {
    Objects.requireNonNull(params);
    Objects.requireNonNull(fileName);

    var target = Path.of(workspaceDir, fileName);
    Files.createDirectories(target);

    IO.println("Starting clone: " + params.url());

    // cloning the repository
    try (Git git = Git.cloneRepository()
            .setURI(params.url())
            .setDirectory(target.toFile())
            .setCredentialsProvider(params.credentials())
            .call()) {

      fetchPr(git, params.credentials(), params.ref());

      checkout(git);
      IO.println("PR fetch and checkout successful");
    }
    return target;
  }

  public void delete(Path directory) throws IOException {
    if (directory == null || !Files.exists(directory)) {
      return;
    }
    try (var paths = Files.walk(directory)) {
      paths.sorted(Comparator.reverseOrder())
              .forEach(path -> {
                try {
                  Files.delete(path);
                } catch (IOException e) {
                  throw new UncheckedIOException(e);
                }
              });
    }

  }
}