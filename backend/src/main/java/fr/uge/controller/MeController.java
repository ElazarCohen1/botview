package fr.uge.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;

import java.util.Map;

@Controller("/api")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class MeController {

  @Get("/me")
  public Map<String, Object> me(Authentication authentication) {
    return Map.of(
            "login", authentication.getName(),
            "attributes", authentication.getAttributes());
  }
}