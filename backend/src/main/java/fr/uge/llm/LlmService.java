package fr.uge.llm;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
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

  public LlmService(LlmInitialiser initialiser){
    this.initialiser = initialiser;
    super();
  }

  @PostConstruct
  void init() {
    Thread.ofPlatform().start(()->{
      try {
        initialiser.initialiseModel();
      } catch (IOException | InterruptedException e) {
        throw new RuntimeException(e);
      }
    });

  }

  public String complete(String prompt) {
    Objects.requireNonNull(prompt);
    try {
      LlamaModel model = initialiser.getModel();
      return model.complete(new InferenceParameters(prompt).setNPredict(512));

    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  void close() {
   IO.println("PREDESTROY START");

    try {
      var model = initialiser.getModel();
      IO.println("MODEL RETRIEVED");
      model.close();
      IO.println("MODEL CLOSED");
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }

    IO.println("PREDESTROY END");
  }

}