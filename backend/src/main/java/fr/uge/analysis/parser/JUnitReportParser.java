
package fr.uge.analysis.parser;

import fr.uge.analysis.model.Diagnostic;
import fr.uge.analysis.model.Severity;
import jakarta.inject.Singleton;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class JUnitReportParser {

  public List<Diagnostic> parse(Path projectDirectory) throws Exception {

    var reportsDirectory = projectDirectory
            .resolve("target")
            .resolve("surefire-reports");

    if (!Files.exists(reportsDirectory)) {
      return List.of();
    }

    var diagnostics = new ArrayList<Diagnostic>();

    try (var files = Files.list(reportsDirectory)) {
      // every file test result begining by TEST-
      var testFiles = files
              .filter(path -> path.getFileName().toString().startsWith("TEST-"))
              .filter(path -> path.toString().endsWith(".xml"))
              .toList();

      for (var file : testFiles) {
        var factory = DocumentBuilderFactory.newInstance();

        factory.setFeature(
                "http://apache.org/xml/features/disallow-doctype-decl",
                true
        );

        var builder = factory.newDocumentBuilder();
        var document = builder.parse(file.toFile());

        NodeList testCases =
                document.getElementsByTagName("testcase");

        // for every test in the file
        for (int i = 0; i < testCases.getLength(); i++) {
          var testCase = (Element) testCases.item(i);
          // the failures tests
          var failures = testCase.getElementsByTagName("failure");
          // the errors tests
          var errors = testCase.getElementsByTagName("error");

          if (failures.getLength() > 0) {
            diagnostics.add(createDiagnostic(
                    testCase,
                    (Element) failures.item(0)
            ));
          } else if (errors.getLength() > 0) {
            diagnostics.add(createDiagnostic(
                    testCase,
                    (Element) errors.item(0)
            ));
          }
        }
      }
    }

    return List.copyOf(diagnostics);
  }

  private Diagnostic createDiagnostic(
          Element testCase,
          Element failure
  ) {
    var className = testCase.getAttribute("classname");
    var testName = testCase.getAttribute("name");

    var message = failure.getAttribute("message");

    if (message.isBlank()) {
      message = failure.getTextContent();
    }

    var file = className.replace('.', '/') + ".java";

    return new Diagnostic(
            file,
            -1,
            Severity.ERROR,
            "Test " + testName + " failed: " + message
    );
  }
}
