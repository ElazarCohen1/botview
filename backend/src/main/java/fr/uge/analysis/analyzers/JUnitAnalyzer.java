
package fr.uge.analysis.analyzers;

import fr.uge.analysis.Analyzer;
import fr.uge.analysis.model.AnalysisResult;
import fr.uge.analysis.model.AnalysisStatus;
import fr.uge.analysis.parser.JUnitReportParser;
import fr.uge.sandbox.SandboxRunner;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.util.List;

@Singleton
public class JUnitAnalyzer implements Analyzer {
  private final JUnitReportParser reportParser;

  private final SandboxRunner sandboxRunner;

  public JUnitAnalyzer(SandboxRunner sandboxRunner, JUnitReportParser reportParser) {
    this.sandboxRunner = sandboxRunner;
    this.reportParser = reportParser;
    super();
  }

  @Override
  public AnalysisResult run(Path projectDirectory) {
    try {
      var result = sandboxRunner.run(
              projectDirectory,
              List.of("mvn", "test")
      );
      
      if (result.timeout()) {
        return failed("JUnit tests timed out");
      }

      var diagnostics = reportParser.parse(projectDirectory);

      if (result.exitCode() != 0) {
        return new AnalysisResult(
                "JUnit",
                AnalysisStatus.FAILED,
                diagnostics,
                "Maven execution failed"
        );
      }

      return new AnalysisResult(
              "JUnit",
              AnalysisStatus.SUCCESS,
              diagnostics,
              null
      );

    } catch (Exception e) {
      return failed(e.getMessage());
    }
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



