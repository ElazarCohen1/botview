package fr.uge.llm;

import de.kherud.llama.InferenceParameters;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Requires;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.util.Objects;

@Requires(notEnv = "test")
@Context
@Singleton
@Requires(notEnv = "test")
public class LlmService {
  // value get the value from the application.yml
  private final LlmInitialiser initialiser;
  private Thread loaderThread;

  public LlmService(LlmInitialiser initialiser) {
    this.initialiser = initialiser;
    super();
  }

  @PostConstruct
  void init() {
    loaderThread = Thread.ofPlatform().daemon().start(() -> {
      try {
        initialiser.initialiseModel();
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

  }

  public String complete(String systemPrompt, String userContent) {
    Objects.requireNonNull(systemPrompt);
    Objects.requireNonNull(userContent);

    try {
      var model = initialiser.getModel();

      var prompt = """
              <|im_start|>system
              %s<|im_end|>
              <|im_start|>user
              %s<|im_end|>
              <|im_start|>assistant
              """.formatted(systemPrompt, userContent);

      var params = new InferenceParameters(prompt)
              .setNPredict(1024)
              .setTemperature(0.1f)
              .setTopP(0.9f)
              .setRepeatPenalty(1.1f)
              .setStopStrings("<|im_end|>");

      return model.complete(params);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  void close() {
    if (loaderThread != null) {
      loaderThread.interrupt();
    }
    initialiser.shutdown();
  }

}