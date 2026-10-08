package fr.uge.controller;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.Objects;

@Controller("/api")
@ExecuteOn(TaskExecutors.BLOCKING)
public class UserController {
  private final GitProvider gitProvider;

  public UserController(GitProvider gitProvider) {
    Objects.requireNonNull(gitProvider);
    this.gitProvider = gitProvider;
  }

  @Get("/user")
  public GitUserDto user(){
    // change after when get the token from the user
    return gitProvider.getUser(null);
  }

}