package fr.uge.nGrok;

import com.ngrok.Session;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import java.net.URL;

@Singleton

public class connectNgrok {
    @EventListener
    public void connectNgrokonStatup(StartupEvent event) throws Exception {
        try {
            var session = Session.withAuthtokenFromEnv().connect();
            var forwarder = session.httpEndpoint()
                    .domain("upscale-sixfold-extruding.ngrok-free.dev")
                    .forward(new URL("http://localhost:5173"));
            System.out.println("Available at: " + forwarder.getUrl());
        } catch (Exception e) {

        }
    }
}
