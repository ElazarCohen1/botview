package fr.uge.llm;


import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Singleton
/*
  thread safe class for downloading the gguf file for the llama.cpp llm
 */
public class LlmInitialiser {
  private final ReentrantLock lock;
  private final Condition condition;
  private final ModelDownloader downloader;
  private final String modelPath;
  private final String modelUrl;
  private LlamaModel model;
  private Status status;
  private Exception initializationError;

  public LlmInitialiser(ModelDownloader downloader,
                        @Value("${llm.model-url}") String modelUrl,
                        @Value("${llm.model-path}") String modelPath) {
    Objects.requireNonNull(modelPath);
    Objects.requireNonNull(modelUrl);
    Objects.requireNonNull(downloader);

    var localLock = new ReentrantLock();
    this.condition = localLock.newCondition();
    this.lock = localLock;
    this.downloader = downloader;
    this.modelPath = modelPath;
    this.modelUrl = modelUrl;
    this.status = Status.NOT_INITIALIZED;
    super();
  }

  private void loadModel() throws IOException, InterruptedException {
    try {
      var path = downloader.ensureModel(Path.of(modelPath), URI.create(modelUrl));
      var loadedModel = new LlamaModel(new ModelParameters().setModel(path.toString()));
      lock.lock();
      try {
        if (status == Status.CLOSED) {
          loadedModel.close();
          return;
        }
        model = loadedModel;
        status = Status.READY;
        condition.signalAll();
      } finally {
        lock.unlock();
      }
    } catch (IOException | InterruptedException e) {
      lock.lock();
      try {
        if (status != Status.CLOSED) {
          initializationError = e;
          status = Status.FAILED;
        }
        initializationError = e;
        status = Status.FAILED;
        condition.signalAll();
      } finally {
        lock.unlock();
      }
      throw e;
    }
  }

  public void shutdown() {
    LlamaModel toClose;
    lock.lock();
    try {
      status = Status.CLOSED;
      toClose = model;
      model = null;
      condition.signalAll();
    } finally {
      lock.unlock();
    }
    if (toClose != null) {
      toClose.close();
    }
  }

  public void initialiseModel() throws IOException, InterruptedException {
    lock.lock();
    try {
      // llm already initialised
      if (status != Status.NOT_INITIALIZED) {
        return;
      }
      status = Status.LOADING;
    } finally {
      lock.unlock();
    }
    loadModel();
  }

  public LlamaModel getModel() throws InterruptedException {

    lock.lock();
    try {

      while (status == Status.NOT_INITIALIZED
              || status == Status.LOADING) {

        condition.await();
      }
      return switch (status) {
        case FAILED -> throw new IllegalStateException("Le chargement du modèle a échoué", initializationError);
        case CLOSED -> throw new IllegalStateException("Le service LLM est arrêté");
        default -> model;
      };

    } finally {
      lock.unlock();
    }
  }

  private enum Status {
    NOT_INITIALIZED, LOADING, READY, FAILED, CLOSED
  }

}
