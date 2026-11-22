package fr.uge.review;

import fr.uge.analysis.AnalysisRunner;
import fr.uge.analysis.RepoCloner;
import fr.uge.analysis.model.AnalysisResult;
import fr.uge.database.Database;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.model.GitWebhookDto;
import fr.uge.llm.LlmParser;
import fr.uge.llm.LlmService;
import fr.uge.llm.model.LlmIssue;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Requires(notEnv = "test")
@Singleton
public class ReviewWorker {

  private static final String SYSTEM_PROMPT = """
          You are a JSON API. You output a single raw JSON object and nothing else.
          
          OUTPUT FORMAT (MANDATORY):
          - Your entire response must be one valid JSON object.
          - The first character of your response MUST be "{" and the last MUST be "}".
          - NEVER use Markdown. NEVER wrap the JSON in ``` or ```json fences.
          - NEVER write any text, greeting or explanation before or after the JSON.
          
          JSON SCHEMA:
          {
            "global_comment": string,
            "issues": "issues": [
             {
               "file": string,          // path of the file, as given in the diff
               "line": integer,         // line number in the new version of the file
               "rule": string,          // short category, e.g. "null-pointer", "security", "resource-leak", "logic-error"
               "description": string,   // what is wrong, why it is a problem, what could happen
               "is_blocking": boolean   // true only for bugs, security flaws or failures that must be fixed before merge
             }
            ]
          }
  
          ROLE:
          You are an expert software engineer doing a strict, objective Pull Request code review.
          Report ONLY genuine technical problems introduced by this Pull Request.
          
          RULES:
          1. Report an issue only if you can state: what is wrong, why it is a problem, and what could happen.
          2. Ignore formatting, naming, comments, documentation and style preferences.
          3. Ignore pre-existing problems; only look at what the diff introduces.
          4. Never invent or speculate. Never create issues just to look useful.
          5. Passing tests/static analysis are not issues, but still inspect the diff for real bugs.
          6. If tests fail, compilation fails, or static analysis finds a genuine problem, report it.
             A test that was not executed is not a failure.
          
          LOOK FOR: bugs, runtime exceptions, null pointers, security vulnerabilities, wrong business logic,
          resource leaks, concurrency problems, performance problems with concrete impact, bad error handling,
          missing validation causing real problems.
          
          GLOBAL COMMENT:
          - No problems: short positive summary + state that no significant issues were identified.
          - Problems: brief summary + the most important problems.
          
          IF NO PROBLEMS, output exactly:
          {"global_comment": "The Pull Request looks good. No significant issues were identified.", "issues": []}
          
          REMINDER: output raw JSON only, starting with "{" and ending with "}". No ``` fences.
          """;

  private final ReviewQueue queue;
  private final GitProviderFactory factory;
  private final RepoCloner cloner;
  private final AnalysisRunner analysisRunner;
  private final LlmService llm;
  private final Database database;
  private final LlmParser llmParser;
  private Thread workerThread;

  public ReviewWorker(ReviewQueue queue,
                      GitProviderFactory factory,
                      RepoCloner cloner,
                      AnalysisRunner analysisRunner,
                      LlmService llm,
                      Database database,
                      LlmParser llmParser) {
    this.queue = queue;
    this.factory = factory;
    this.cloner = cloner;
    this.analysisRunner = analysisRunner;
    this.llm = llm;
    this.database = database;
    this.llmParser = llmParser;
    super();
  }

  @EventListener
  public void onApplicationEvent(StartupEvent event) {
    workerThread = Thread.ofPlatform().name("review-worker").start(this::consumeQueue);
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

  private int insertOrUpdatePr(GitWebhookDto event) throws SQLException {
    // throw if we dont have the repo in the bdd bit get a pull request (not normal)
    var repo = database.getRepositoryQueries().findByUrl(event.repoUrl()).orElseThrow();
    var databasePr = database.getPullRequestQueries();
    return switch (event.action().toLowerCase(Locale.ROOT)) {
      case "opened" -> databasePr.insertPullRequest(event.prNumber(), event.PullRequestUrl(), "OPENED", repo.id());

      case "reopened" ->
              databasePr.updatePullRequest(event.prNumber(), repo.id(), "REOPENED").orElseThrow(SQLException::new);

      case "synchronize" ->
              databasePr.updatePullRequest(event.prNumber(), repo.id(), "SYNCHRONIZE").orElseThrow(SQLException::new);

      default -> throw new IllegalArgumentException("Action " + event.action() + " is not supported");
    };


  }

  private String generateUserContent(String diff, String staticAnalysis) {
    return """
            Review the following Pull Request.
            The diff contains the modifications introduced by this PR.
            The static analysis contains the results of automated checks
            executed on this PR.
            <diff>
            %s
            </diff>
            
            <static_analysis>
            %s
            </static_analysis>
            """.formatted(diff, staticAnalysis);
  }

  private void process(GitWebhookDto event) throws Exception {
    var git = factory.getProvider(event.provider());
    var fileName = event.repo().replace('/', '_') + "-" + event.prNumber() + "-" + event.headSha().substring(0, 7);
    Path dir = null;
    int prId = 1;
    try {
      prId = insertOrUpdatePr(event);
    } catch (SQLException e) {
      IO.println("erreur inserting the pr " + e);
    }
    try {
      dir = cloner.clone(git.cloneSpec(event), fileName);
      var results = analysisRunner.run(dir);
      var diff = git.getPullRequestDiff(event.diffUrl());
      var response = llm.complete(SYSTEM_PROMPT, generateUserContent(diff, formatStaticAnalysis(results)));
      IO.println(response);
      var parsedResponse = llmParser.parseLlmResponse(response);
      var isBlocking = parsedResponse.issues().stream().anyMatch(LlmIssue::blocking);
      var reviewId = database.getReviewQueries().insertReview(parsedResponse.globalComment(), ReviewStatus.DONE, isBlocking, prId);
      IO.println("review inserted");
    } finally {
      if (dir != null) {
        cloner.delete(dir);
      }
    }
  }

  private String formatStaticAnalysis(List<AnalysisResult> results) {
    return results.stream()
            .flatMap(r ->
                    r.diagnostics()
                            .stream()
                            .map(d ->
                                    "[%s] %s:%d %s".formatted(r.tool(), d.file(), d.line(), d.message())
                            )
            ).distinct().collect(Collectors.joining("\n"));
  }

  @PreDestroy
  void stop() {
    if (workerThread != null) {
      workerThread.interrupt();
    }
  }
}
