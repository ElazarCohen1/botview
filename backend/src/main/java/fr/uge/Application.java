package fr.uge;

import io.micronaut.runtime.Micronaut;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Application {

  static void main(String[] args) throws IOException, URISyntaxException {
    var dataDir = baseDirectory().resolve("data");
    Files.createDirectories(dataDir);
    System.setProperty("db.path", dataDir.resolve("botview.duckdb").toString());
    Micronaut.run(Application.class, args);
  }

  private static Path baseDirectory() throws URISyntaxException {
    var location = Path.of(Application.class.getProtectionDomain().
            getCodeSource().getLocation().toURI());
    if (Files.isRegularFile(location)) {
      return location.toAbsolutePath().getParent();
    }
    return Path.of("").toAbsolutePath();
  }
}