package fr.uge.analysis;

import fr.uge.analysis.model.AnalysisResult;
import fr.uge.analysis.model.AnalysisStatus;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
public class AnalysisRunner {
  private final List<Analyzer> analysers;

  public AnalysisRunner(List<Analyzer> analyzers) {
    Objects.requireNonNull(analyzers);
    for (var analyser : analyzers) {
      if (analyser == null) {
        throw new NullPointerException();
      }
    }
    this.analysers = List.copyOf(analyzers); // safe copy of the list
    super();
  }

  public List<AnalysisResult> run(Path projectDirectory) {
    Objects.requireNonNull(projectDirectory);
    var results = new ConcurrentLinkedQueue<AnalysisResult>();

    var threads = analysers.stream().map(analyser ->
            Thread.ofPlatform().start(() -> {
              try {
                IO.println("test lancer");
                results.add(analyser.run(projectDirectory));
              } catch (Exception e) {
                results.add(new AnalysisResult(
                        analyser.getClass().getSimpleName(),
                        AnalysisStatus.FAILED,
                        List.of(),
                        e.getMessage()
                ));
              }
            })
    ).toList();

    // we wait all the tests to finish
    for (var thread : threads) {
      try {
        thread.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Analysis interrupted", e);
      }
    }

    return List.copyOf(results);
  }
}
