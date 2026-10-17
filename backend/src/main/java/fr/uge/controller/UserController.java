package fr.uge.controller;

import fr.uge.git.GitProvider;
import fr.uge.git.GitProviderFactory;
import fr.uge.git.GitProviderType;
import fr.uge.git.model.GitUserDto;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.Objects;

@Controller("/api")
@ExecuteOn(TaskExecutors.BLOCKING)
public class UserController {
  private final GitProviderFactory factoryProvider;

  public UserController(GitProviderFactory factoryProvider) {
    Objects.requireNonNull(factoryProvider);
    this.factoryProvider = factoryProvider;
  }

  @Get("/user")
  public GitUserDto user(@QueryValue GitProviderType provider){
    Objects.requireNonNull(provider);
    GitProvider gitProvider = factoryProvider.getProvider(provider);
    return gitProvider.getUser();
  }

}