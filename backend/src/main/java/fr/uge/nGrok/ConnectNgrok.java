
package fr.uge.nGrok;

import com.ngrok.Session;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.context.event.StartupEvent;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;

import java.net.URI;

@Singleton
@Requires(property = "ngrok.enabled", value = "true")
public class ConnectNgrok implements ApplicationEventListener<StartupEvent> {

  private final String url;

  public ConnectNgrok(@Value("${ngrok.url:}") String url) {
    this.url = url;
  }

  @Override
  public void onApplicationEvent(@NonNull StartupEvent event) {
    try {
      var session = Session.withAuthtokenFromEnv().connect();
      var forwarder = session.httpEndpoint()
              .domain(url)
              .forward(new URI("http://localhost:8080").toURL());

      System.out.println("Available at: " + forwarder.getUrl());

    } catch (Exception e) {
      throw new RuntimeException("Error initializing ngrok", e);
    }
  }
}
