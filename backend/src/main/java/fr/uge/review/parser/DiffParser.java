package fr.uge.review.parser;

import fr.uge.llm.model.LlmIssue;
import fr.uge.review.model.ChangeStatus;
import fr.uge.review.model.ParsedFile;
import fr.uge.review.model.ParsedLine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public final class DiffParser {
  private static final Pattern FILE_HEADER = Pattern.compile("^diff --git a/(.+) b/(.+)$");
  private static final Pattern HUNK = Pattern.compile("^@@ -(\\d+)(?:,\\d+)? \\+(\\d+)(?:,\\d+)? @@.*");

  private record RemovedLine(int lineNumber, String value) {}
  private record AddedLine(int lineNumber, String value) {}

  public static List<ParsedFile> parse(String diff) {
    Objects.requireNonNull(diff);

    var files = new ArrayList<ParsedFile>();
    var lines = new ArrayList<ParsedLine>();
    var deleted = new ArrayList<RemovedLine>();
    var added = new ArrayList<AddedLine>();

    String path = null;
    var status = ChangeStatus.MODIFIED;
    int oldNo = 0;
    int newNo = 0;
    boolean inHunk = false;

    for (var line : diff.split("\n")) {
      var header = FILE_HEADER.matcher(line);

      if (header.matches()) {
        flushChanges(lines, deleted, added);

        if (path != null) {
          files.add(new ParsedFile(path, status, lines));
        }

        path = header.group(2);
        status = ChangeStatus.MODIFIED;
        lines = new ArrayList<>();
        inHunk = false;
        continue;
      }

      if (path == null) continue;

      if (!inHunk) {
        if (line.startsWith("new file mode")) status = ChangeStatus.ADDED;
        else if (line.startsWith("deleted file mode")) status = ChangeStatus.DELETED;
        else if (line.startsWith("rename from")) status = ChangeStatus.RENAMED;
      }

      var hunk = HUNK.matcher(line);

      if (hunk.matches()) {
        flushChanges(lines, deleted, added);
        oldNo = Integer.parseInt(hunk.group(1));
        newNo = Integer.parseInt(hunk.group(2));
        inHunk = true;
        continue;
      }

      if (!inHunk || line.isEmpty()) continue;

      switch (line.charAt(0)) {
        case '+' -> added.add(new AddedLine(newNo++, line.substring(1)));
        case '-' -> deleted.add(new RemovedLine(oldNo++, line.substring(1)));
        case ' ' -> {
          flushChanges(lines, deleted, added);
          oldNo++;
          newNo++;
        }
        default -> { }
      }
    }

    flushChanges(lines, deleted, added);

    if (path != null) {
      files.add(new ParsedFile(path, status, lines));
    }

    return List.copyOf(files);
  }



  private static void flushChanges(
          List<ParsedLine> lines,
          List<RemovedLine> deleted,
          List<AddedLine> added
  ) {
    int paired = Math.min(deleted.size(), added.size());

    for (int i = 0; i < paired; i++) {
      var oldLine = deleted.get(i);
      var newLine = added.get(i);

      lines.add(new ParsedLine(
              newLine.lineNumber(),
              ChangeStatus.MODIFIED,
              oldLine.value(),
              newLine.value()
      ));
    }

    for (int i = paired; i < deleted.size(); i++) {
      var line = deleted.get(i);
      lines.add(new ParsedLine(
              line.lineNumber(),
              ChangeStatus.DELETED,
              line.value(),
              null
      ));
    }

    for (int i = paired; i < added.size(); i++) {
      var line = added.get(i);
      lines.add(new ParsedLine(
              line.lineNumber(),
              ChangeStatus.ADDED,
              null,
              line.value()
      ));
    }

    deleted.clear();
    added.clear();
  }


  public static List<ParsedFile> filterProblematicFiles(
          List<ParsedFile> files,
          List<LlmIssue> issues
  ) {
    Objects.requireNonNull(files);
    Objects.requireNonNull(issues);

    var result = new ArrayList<ParsedFile>();

    for (var file : files) {
      var problematicLines = file.lines().stream()
              .filter(line -> issues.stream().anyMatch(issue ->
                      issue.file().equals(file.path())
                              && issue.line() == line.lineNumber()
              ))
              .toList();

      if (!problematicLines.isEmpty()) {
        result.add(new ParsedFile(
                file.path(),
                file.status(),
                problematicLines
        ));
      }
    }

    return List.copyOf(result);
  }
}