
package fr.uge.review.service;

import fr.uge.database.CommentOnQueries;
import fr.uge.database.FileQueries;
import fr.uge.database.ModifiedLineQueries;
import fr.uge.database.ReviewQueries;
import fr.uge.llm.model.LlmIssue;
import fr.uge.review.ReviewStatus;
import fr.uge.review.model.ParsedFile;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Singleton
public class ReviewService {
  private final CommentOnQueries commentOnQueries;
  private final FileQueries fileQueries;
  private final ModifiedLineQueries modifiedLineQueries;
  private final ReviewQueries reviewQueries;

  public ReviewService(
          CommentOnQueries commentOnQueries,
          FileQueries fileQueries,
          ModifiedLineQueries modifiedLineQueries,
          ReviewQueries reviewQueries
  ) {
    Objects.requireNonNull(commentOnQueries);
    Objects.requireNonNull(fileQueries);
    Objects.requireNonNull(modifiedLineQueries);
    Objects.requireNonNull(reviewQueries);
    this.commentOnQueries = commentOnQueries;
    this.fileQueries = fileQueries;
    this.modifiedLineQueries = modifiedLineQueries;
    this.reviewQueries = reviewQueries;
    super();
  }

  @Transactional
  public void saveReview(
          int pullRequestId,
          String globalComment,
          List<ParsedFile> files,
          List<LlmIssue> issues
  ) throws SQLException {

    Objects.requireNonNull(globalComment);
    Objects.requireNonNull(files);
    Objects.requireNonNull(issues);

    var isBlocking = issues.stream().anyMatch(LlmIssue::blocking);
    // insert the review
    var reviewId = reviewQueries.insertReview(
            globalComment,
            ReviewStatus.DONE,
            isBlocking,
            pullRequestId
    );

    var lineIds = new HashMap<String, Map<Integer, Integer>>();
    // insert every files
    for (var file : files) {
      var fileId = fileQueries.insert(
              extractFileName(file.path()),
              file.path(),
              file.status()
      );

      fileQueries.associateWithPullRequest(pullRequestId, fileId);

      var fileLines = new HashMap<Integer, Integer>();

      for (var line : file.lines()) {
        var lineId = modifiedLineQueries.insert(
                fileId,
                line.lineNumber(),
                line.status(),
                line.oldValue(),
                line.newValue()
        );

        fileLines.put(line.lineNumber(), lineId);
      }

      lineIds.put(file.path(), fileLines);
    }

    for (var issue : issues) {
      var fileLines = lineIds.get(issue.file());

      if (fileLines == null) {
        continue;
      }

      var lineId = fileLines.get(issue.line());

      if (lineId == null) {
        continue;
      }

      commentOnQueries.insert(
              reviewId,
              lineId,
              false,
              issue.description()
      );
    }

  }

  private static String extractFileName(String path) {
    var index = path.lastIndexOf('/');
    return path.substring(index + 1);
  }
}
