package fr.uge.llm;

import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;


@Singleton
public class ModelDownloader {

  /**
   *
   * @param target the folder we want to put the gguf file
   * @param url    the url we load the gguf file
   * @return the target
   * @throws IOException          we create the directories so it may return an exception
   * @throws InterruptedException throws
   */
  public Path ensureModel(Path target, URI url) throws IOException, InterruptedException {
    Objects.requireNonNull(target);
    Objects.requireNonNull(url);

    if (Files.exists(target)) {
      return target;
    }

    Files.createDirectories(target.toAbsolutePath().getParent());


    var temporaryFile = target.resolveSibling(target.getFileName() + ".txt");

    HttpResponse<Path> response;
    try (var client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build()) {
      var request = HttpRequest.newBuilder(url).GET().build();

      // writing in the txt file
      response = client.send(request, HttpResponse.BodyHandlers.ofFile(temporaryFile));
    }
    if (response.statusCode() != 200) {
      Files.deleteIfExists(temporaryFile);
      throw new IOException("downloading failed ,  HTTP code" + response.statusCode());
    }

    Files.move(temporaryFile, target, StandardCopyOption.REPLACE_EXISTING);
    return target;
  }
}