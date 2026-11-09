package fr.uge.sandbox;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Singleton

public class SandboxInitializer {

    @EventListener
    public void initSandbox(StartupEvent event){
        Objects.requireNonNull(event);
        var process = new ProcessBuilder();
        try {
            process.command("bwrap","--version").start();
            IO.println(" BubbleRead is installed");


        }catch (IOException e){
            IO.println(" BubbleRead not installed");
            return;
        }
        var path = Path.of("").toAbsolutePath() + "/data";

        var bwrapArg = List.of(
                "bwrap",
                "--ro-bind" , "/usr",  "/usr",
                "--proc" , "/proc",
                "--dir", "/sandbox",
                "--bind", path, "/sandbox",
                "--dev" ,"/dev",
                "/usr/bin/echo", "Lancement SandBox");
        try{
            process.command(bwrapArg).inheritIO().start();
        }catch (IOException e){

        }


    }


}
