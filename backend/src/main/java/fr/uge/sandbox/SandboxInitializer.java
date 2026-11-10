package fr.uge.sandbox;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Singleton
public class SandboxInitializer {
    @EventListener
    public void initSandbox(StartupEvent event){
        Objects.requireNonNull(event);

        if(!verificationBubbleWrapIsPresent()){
            return;
        }
        launchSandboxTest();
    }


    private boolean verificationBubbleWrapIsPresent(){
        var process = new ProcessBuilder();
        try {
            process.command("bwrap","--version").start();
            IO.println(" BubbleWrap is installed");
            return true;

        }catch (IOException e){
            IO.println(" BubbleWrap not installed " +e.getMessage() );
            return false;
        }
    }
    private void launchSandboxTest(){
        var process = new ProcessBuilder();
        var path = Path.of("../backend/data/"); // start at target
        var pathTestSandbox = path.resolve("testSandbox");
        var pathTestSandboxOut = path.resolve("testSandboxOut");
        var tempOut = createTempDirectoryBubbleWrap(pathTestSandboxOut); // temporary file for all test
        if(tempOut == null){
            return;
        }
        var bwrapArg = List.of(
                "bwrap",
                "--ro-bind" , "/usr",  "/usr",
                "--proc" , "/proc",
                "--symlink", "usr/bin", "/bin",
                "--symlink", "usr/lib", "/lib",
                "--symlink", "usr/lib64", "/lib64",
                "--bind", pathTestSandbox.toString(), "/test",
                "--dev" ,"/dev",
                "--bind", tempOut.toString(), "/out",
                "/bin/sh","-c","cat /test/pullRequestTest.txt > /out/resultat.txt"
        );
        launchCommandBwrap(process, bwrapArg);
    }

    private void launchCommandBwrap(ProcessBuilder process, List<String> bwrapArg){
        try{
            try{
                process.command(bwrapArg).start().waitFor();
            }catch (InterruptedException e){

            }
            //deleteTempDirectoryBubbleWrap(tempOut);
        }catch (IOException e){
            IO.print("Error bwrap command " + e.getMessage());
        }
    }

    private Path createTempDirectoryBubbleWrap(Path path){
        try{
            return  Files.createTempDirectory(path,"sandbox_result");
        }catch (IOException e){
            IO.println("Error creating directory " + e.getMessage());
            return  null;
        }
    }

    private void deleteTempDirectoryBubbleWrap(Path path){
        Objects.requireNonNull(path);
        try {
            Files.deleteIfExists(path);
        }catch (IOException e){
            IO.println("Problem deleting directory " + e.getMessage());
        }
    }
}
