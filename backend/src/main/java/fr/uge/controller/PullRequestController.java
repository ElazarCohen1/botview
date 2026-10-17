//package fr.uge.controller;
//
//import io.micronaut.http.annotation.Controller;
//import io.micronaut.security.annotation.Secured;
//import io.micronaut.security.rules.SecurityRule;
//
//import java.util.Objects;
//
//@Controller("/api/pullRequest")
//@Secured(SecurityRule.IS_AUTHENTICATED)
//public class PullRequestController<Request> {
//  private final Request request;
//
//  public PullRequestController(Request request) {
//    Objects.requireNonNull(request);
//    this.request = request;
//    super();
//  }
//
//  @Get("/{repo}")
//  public List<GitPullRequestDto> pullRequests(String repo){
//    Objects.requireNonNull(repo);
//    return githubClient.getPullRequests(repo);
//  }
//}
