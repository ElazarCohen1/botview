package fr.uge.model;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * represent a pull request in the bdd
 *
 * @param pullId       the id of the pr
 * @param number       the number of the pr on git
 * @param name         the name
 * @param url          the url on git
 * @param createdAt    created at
 * @param updatedAt    the last update of the pr
 * @param repositoryId the id of the repository linked
 */
@Serdeable
public record PullRequest(
        int pullId,
        int number,
        @NotNull String name,
        @NotNull String url,
        @NotNull Instant createdAt,
        @NotNull Instant updatedAt,
        int repositoryId,
        @NotNull String action
) {
}
