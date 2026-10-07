package fr.uge.controller;

import fr.uge.git.GitProvider;
import fr.uge.git.model.GitUserDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

@Controller("/api")
@ExecuteOn(TaskExecutors.BLOCKING)
public class UserController {
  private final GitProvider gitProvider;

  public UserController(GitProvider gitProvider) {
    this.gitProvider = gitProvider;
  }

  @Get("/user")
  public GitUserDto user(){
  return gitProvider.getUser(null);
  }

}