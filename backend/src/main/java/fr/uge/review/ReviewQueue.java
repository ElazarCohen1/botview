package fr.uge.review;

import fr.uge.git.model.GitWebhookDto;
import jakarta.inject.Singleton;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * thread safe class for the analysis queue
 */
@Singleton
public class ReviewQueue {
  private final BlockingQueue<GitWebhookDto> queue;


  /**
   * Create a new review Queue
   */
  public ReviewQueue() {
    this.queue = new LinkedBlockingQueue<>();
    super();
  }


  /**
   * submit a job into the queue
   *
   * @param job
   */
  public void submit(GitWebhookDto job) {
    Objects.requireNonNull(job);
    queue.add(job);
  }

  public GitWebhookDto take() throws InterruptedException {
    return queue.take();
  }
}
