package fr.uge.git.github;

import fr.uge.git.model.GitUserDto;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.List;
import java.util.Map;

@Named("github")
@Singleton
public class GithubAuthenticationMapper implements OauthAuthenticationMapper {

  private final HttpClient client;

  public GithubAuthenticationMapper(@Client("https://api.github.com") HttpClient client) {
    this.client = client;
  }

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse, @Nullable State state) {
    String accessToken = tokenResponse.getAccessToken();
    // get the user
    HttpRequest<?> request = HttpRequest.GET("/user")
            .header("Authorization", "Bearer " + accessToken)
            .header("User-Agent", "pr-review-bot")
            .header("Accept", "application/vnd.github+json");

    // Publishers result come after
    return Publishers.map(
            // GithubUser.class to transform json into the record GithubUser
            client.retrieve(request, GitUserDto.class),
            user -> AuthenticationResponse.success(
                    user.login(),
                    List.of("ROLE_USER"),
                    Map.of("id", user.id(),"avatarUrl", user.avatarUrl())));
  }
}