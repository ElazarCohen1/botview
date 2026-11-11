
package fr.uge.sandbox;

import fr.uge.sandbox.model.CommandResult;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Singleton
public class SandboxRunner {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(5);
  private static final String BWRAP = "/usr/bin/bwrap";
  private static final String SANDBOX_JAVA_HOME = "/opt/java";

  public CommandResult run(Path project, List<String> command)
          throws IOException, InterruptedException {
    return run(project, command, DEFAULT_TIMEOUT);
  }

  public CommandResult run(
          Path project, List<String> command, Duration timeout)
          throws IOException, InterruptedException {

    Objects.requireNonNull(project);
    Objects.requireNonNull(command);
    Objects.requireNonNull(timeout);

    if (command.isEmpty()) {
      throw new IllegalArgumentException("Command cannot be empty");
    }
    if (timeout.isZero() || timeout.isNegative()) {
      throw new IllegalArgumentException("Timeout must be positive");
    }

    var directory = project.toRealPath();
    if (!Files.isDirectory(directory)) {
      throw new IllegalArgumentException(
              "Project directory does not exist: " + project);
    }

    var output = Files.createTempDirectory("sandbox-output-");
    try {
      return execute(directory, command, timeout, output);
    } finally {
      deleteDirectory(output);
    }
  }

  private CommandResult execute(
          Path project, List<String> command,
          Duration timeout, Path output)
          throws IOException, InterruptedException {

    var process = startProcess(project, command, output);
    boolean finished = process.waitFor(
            timeout.toMillis(), TimeUnit.MILLISECONDS);

    if (!finished) {
      killProcessTree(process);
    }

    return buildResult(process, finished, output);
  }

  private Process startProcess(
          Path project, List<String> command, Path output)
          throws IOException {

    var builder = new ProcessBuilder(buildCommand(project, command));
    builder.redirectOutput(output.resolve("stdout.txt").toFile());
    builder.redirectError(output.resolve("stderr.txt").toFile());

    return builder.start();
  }

  private List<String> buildCommand(Path project, List<String> command)
          throws IOException {

    var args = new ArrayList<String>();
    args.add(BWRAP);

    configureIsolation(args);
    configureFilesystem(args, project);
    configureEnvironment(args);

    args.add("--");
    args.addAll(command);

    return args;
  }

  private void configureIsolation(List<String> args) {
    args.addAll(List.of(
            "--unshare-all",
            "--die-with-parent",
            "--new-session"
    ));
  }

  private void configureFilesystem(List<String> args, Path project)
          throws IOException {

    bindReadOnly(args, "/usr");
    bindReadOnly(args, "/bin");
    bindReadOnly(args, "/lib");
    bindReadOnly(args, "/lib64");
    bindReadOnly(args, "/etc");

    configureJdk(args);
    configureMavenCache(args);
    configureVirtualFilesystems(args);
    configureWorkspace(args, project);
  }

  private void configureJdk(List<String> args) throws IOException {
    var javaHome = Path.of(System.getProperty("java.home")).toRealPath();
    var javac = javaHome.resolve("bin/javac");

    if (!Files.isExecutable(javac)) {
      throw new IOException(
              "A JDK is required to run Maven tests. No javac found in: "
                      + javaHome);
    }

    args.add("--dir");
    args.add("/opt");

    args.add("--ro-bind");
    args.add(javaHome.toString());
    args.add(SANDBOX_JAVA_HOME);
  }

  private void configureMavenCache(List<String> args) throws IOException {
    var hostRepository = Path.of(
            System.getProperty("user.home"),
            ".m2",
            "repository"
    );

    if (!Files.isDirectory(hostRepository)) {
      return;
    }

    var repository = hostRepository.toRealPath();

    args.add("--dir");
    args.add("/home/sandbox/.m2");

    args.add("--ro-bind");
    args.add(repository.toString());
    args.add("/home/sandbox/.m2/repository");
  }

  private void configureVirtualFilesystems(List<String> args) {
    args.addAll(List.of(
            "--proc", "/proc",
            "--dev", "/dev",
            "--tmpfs", "/tmp",
            "--dir", "/home",
            "--dir", "/home/sandbox"
    ));
  }

  private void configureWorkspace(List<String> args, Path project) {
    args.add("--bind");
    args.add(project.toString());
    args.add("/workspace");

    args.add("--chdir");
    args.add("/workspace");
  }

  private void configureEnvironment(List<String> args) {
    args.add("--clearenv");

    addEnvironment(args, "HOME", "/home/sandbox");
    addEnvironment(args, "JAVA_HOME", SANDBOX_JAVA_HOME);
    addEnvironment(args, "PATH", SANDBOX_JAVA_HOME + "/bin:/usr/bin:/bin");
  }

  private void addEnvironment(
          List<String> args, String name, String value) {

    if (value == null || value.isBlank()) {
      return;
    }

    args.add("--setenv");
    args.add(name);
    args.add(value);
  }

  private void bindReadOnly(List<String> args, String directory) {
    if (!Files.exists(Path.of(directory))) {
      return;
    }

    args.addAll(List.of("--ro-bind", directory, directory));
  }

  private CommandResult buildResult(
          Process process, boolean finished, Path output)
          throws IOException {

    int exitCode = finished ? process.exitValue() : -1;
    var stdout = readOutput(output.resolve("stdout.txt"));
    var stderr = readOutput(output.resolve("stderr.txt"));

    return new CommandResult(exitCode, stdout, stderr, !finished);
  }

  private String readOutput(Path file) throws IOException {
    return Files.readString(file, StandardCharsets.UTF_8);
  }

  private void killProcessTree(Process process)
          throws InterruptedException {

    var descendants = process.toHandle().descendants().toList();
    descendants.forEach(ProcessHandle::destroyForcibly);

    process.destroyForcibly();
    process.waitFor();
  }

  private void deleteDirectory(Path directory) throws IOException {
    try (var paths = Files.walk(directory)) {
      var reversedPaths = paths
              .sorted(java.util.Comparator.reverseOrder())
              .toList();

      for (var path : reversedPaths) {
        Files.deleteIfExists(path);
      }
    }
  }
}
