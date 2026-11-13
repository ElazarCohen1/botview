
package fr.uge.review;

import fr.uge.analysis.AnalysisRunner;
import fr.uge.analysis.RepoCloner;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.model.GitWebhookDto;
import fr.uge.llm.LlmService;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

import java.util.Objects;

@Singleton
public class ReviewWorker {

  private final ReviewQueue queue;
  private final GitProviderFactory factory;
  private final RepoCloner cloner;
  private final AnalysisRunner analysisRunner;
  private final LlmService llm;

  private Thread workerThread;

  public ReviewWorker(ReviewQueue queue, GitProviderFactory factory, RepoCloner cloner, AnalysisRunner analysisRunner,
      LlmService llm) {
    this.queue = queue;
    this.factory = factory;
    this.cloner = cloner;
    this.analysisRunner = analysisRunner;
    this.llm = llm;
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
          IO.println("Error processing review: " + e.getMessage());
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }



  private  String generatePrompt(String fileDiff, String staticAnalysis){
    Objects.requireNonNull(fileDiff);
    Objects.requireNonNull(staticAnalysis);
    return " Analyse la différence entre les 2  commits ainsi que le résultats de l'analyse statique\n." +
            " Voici les différences : \n" + fileDiff  +
            "\net voici les analyses : \n" + staticAnalysis;
  }

  private void process(GitWebhookDto event) throws Exception {
    var git = factory.getProvider(event.provider());
    var fileName = event.repo().replace('/', '_') + "-"
        + event.prNumber() + "-"
        + event.headSha().substring(0, 7);

    var dir = cloner.clone(git.cloneSpec(event), fileName);
    try {
      var results = analysisRunner.run(dir);
      results.forEach(IO::println);
      var diff = git.getPullRequestDiff(event.repo(), event.prNumber());
      var response = llm.complete(generatePrompt(diff,results.toString()));
      IO.println(response);
    } finally {
      cloner.delete(dir);
    }
  }

  @PreDestroy
  void stop() {
    if (workerThread != null) {
      workerThread.interrupt();
    }
  }
}
