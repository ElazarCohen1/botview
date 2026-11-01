package fr.uge.nGrok;

import com.ngrok.Session;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.net.URI;

@Singleton
@Requires(property = "ngrok.enabled", value = "true")
public class connectNgrok {
    @EventListener
    public void connectNgrokOnStartup(StartupEvent event) throws Exception {
        try {
          com.ngrok.Forwarder.Endpoint forwarder;
          var session = Session.withAuthtokenFromEnv().connect();
          forwarder = session.httpEndpoint()
                    .domain("upscale-sixfold-extruding.ngrok-free.dev")
                    .forward(new URI("http://localhost:5173").toURL());
         IO.println("Available at: " + forwarder.getUrl());
        } catch (Exception e) {
            throw new IOException("error initialing the ngrok");
        }
    }
}
