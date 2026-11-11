
package fr.uge.analysis.analyzers;

import fr.uge.analysis.Analyzer;
import fr.uge.analysis.model.AnalysisResult;
import fr.uge.analysis.model.AnalysisStatus;
import fr.uge.sandbox.SandboxRunner;
import fr.uge.sandbox.model.CommandResult;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.util.List;

@Singleton
public class JUnitAnalyzer implements Analyzer {

  private final SandboxRunner sandboxRunner;

  public JUnitAnalyzer(SandboxRunner sandboxRunner) {
    this.sandboxRunner = sandboxRunner;
    super();
  }

  @Override
  public AnalysisResult run(Path projectDirectory) {
    try {
      var result = sandboxRunner.run(
              projectDirectory,
              List.of("mvn", "test")
      );
      return createResult(result);
    } catch (Exception e) {
      return failed(e.getMessage());
    }
  }

  private AnalysisResult createResult(CommandResult result) {
    if (result.timeout()) {
      return failed("JUnit tests timed out");
    }
    if (result.exitCode() != 0) {
      return failed(result.stderr());
    }
    return new AnalysisResult(
            "JUnit",
            AnalysisStatus.SUCCESS,
            List.of(),
            null
    );
  }

  private AnalysisResult failed(String message) {
    return new AnalysisResult(
            "JUnit",
            AnalysisStatus.FAILED,
            List.of(),
            message
    );
  }
}
