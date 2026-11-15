
package fr.uge.review;

import fr.uge.analysis.AnalysisRunner;
import fr.uge.analysis.RepoCloner;
import fr.uge.database.Database;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.model.GitWebhookDto;
import fr.uge.llm.LlmService;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Locale;

@Singleton
public class ReviewWorker {

  private final ReviewQueue queue;
  private final GitProviderFactory factory;
  private final RepoCloner cloner;
  private final AnalysisRunner analysisRunner;
  private final LlmService llm;
  private final Database database;

  private Thread workerThread;

  public ReviewWorker(ReviewQueue queue,
                      GitProviderFactory factory,
                      RepoCloner cloner,
                      AnalysisRunner analysisRunner,
                      LlmService llm,
                      Database database) {
    this.queue = queue;
    this.factory = factory;
    this.cloner = cloner;
    this.analysisRunner = analysisRunner;
    this.llm = llm;
    this.database = database;
    super();
  }

  @EventListener
  public void onApplicationEvent(StartupEvent event) {
    workerThread = Thread.ofPlatform()
            .name("review-worker")
            .start(this::consumeQueue);
  }

  private void consumeQueue() {
    try {
      while (!Thread.currentThread().isInterrupted()) {
        var job = queue.take();
        try {
          process(job);
        } catch (Exception e) {
          IO.println("Error processing the review : " + e);
          return;
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private void insertOrUpdatePr(GitWebhookDto event) throws SQLException {
    // throw if we dont have the repo in the bdd bit get a pull request (not normal)Z
    var repo = database.getRepositoryQueries().findByUrl(event.repoUrl()).orElseThrow();
    var databasePr = database.getPullRequestQueries();
    switch (event.action().toLowerCase(Locale.ROOT)) {
      case "opened" -> databasePr.insertPullRequest(
              event.prNumber(),
              event.PullRequestUrl(),
              "OPENED",
              repo.id()
      );

      case "reopened" -> databasePr.updatePullRequest(
              event.prNumber(),
              repo.id(),
              "REOPENED"
      );

      case "synchronize" -> databasePr.updatePullRequest(
              event.prNumber(),
              repo.id(),
              "SYNCHRONIZE"
      );

      default -> throw new IllegalArgumentException(
              "Action " + event.action() + " is not supported"
      );
    }

    if (event.action().equals("synchronize")) {
      IO.println("pr updated");
    }


  }

  private void process(GitWebhookDto event) throws Exception {
    var git = factory.getProvider(event.provider());
    var fileName = event.repo().replace('/', '_') + "-"
            + event.prNumber() + "-"
            + event.headSha().substring(0, 7);
    Path dir = null;
    try {
      insertOrUpdatePr(event);
      dir = cloner.clone(git.cloneSpec(event), fileName);
      var results = analysisRunner.run(dir);
      results.forEach(IO::println);
    } finally {
      if (dir != null) {
        cloner.delete(dir);
      }
    }
  }

  @PreDestroy
  void stop() {
    if (workerThread != null) {
      workerThread.interrupt();
    }
  }
}
