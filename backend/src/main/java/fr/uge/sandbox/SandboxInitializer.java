
package fr.uge.sandbox;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import java.io.IOException;

@Singleton
public class SandboxInitializer {

  private static final String BWRAP = "/usr/bin/bwrap";

  @EventListener
  public void initSandbox(StartupEvent event) {
    if (!isBubbleWrapAvailable()) {
      IO.println("WARNING: Bubblewrap is not installed.");
      return;
    }
    IO.println("Bubblewrap is available.");
  }

  private boolean isBubbleWrapAvailable() {
    try {
      var process = new ProcessBuilder(BWRAP, "--version")
              .redirectErrorStream(true)
              .start();
      return process.waitFor() == 0;
    } catch (IOException e) {
      return false;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return false;
    }
  }
}
