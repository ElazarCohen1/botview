
package fr.uge.analysis.analyzers;

import fr.uge.analysis.Analyzer;
import fr.uge.analysis.model.AnalysisResult;
import fr.uge.analysis.model.AnalysisStatus;
import fr.uge.analysis.parser.JUnitReportParser;
import fr.uge.sandbox.SandboxRunner;
import jakarta.inject.Singleton;

import java.nio.file.Files;
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
      IO.println("Project directory: " + projectDirectory);
      IO.println("pom.xml exists: "
              + Files.exists(projectDirectory.resolve("pom.xml")));

      var result = sandboxRunner.run(
              projectDirectory,
              List.of("mvn", "test")
      );

      IO.println("Exit code: " + result.exitCode());
      IO.println("Timeout: " + result.timeout());

      IO.println("========== STDOUT ==========");
      IO.println(result.stdout());

      IO.println("========== STDERR ==========");
      IO.println(result.stderr());

      if (result.timeout()) {
        return failed("JUnit tests timed out");
      }

      var diagnostics = reportParser.parse(projectDirectory);

      IO.println("Diagnostics: " + diagnostics);

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
      e.printStackTrace();
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



