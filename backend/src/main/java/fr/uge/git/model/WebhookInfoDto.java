package fr.uge.git.model;


import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

/**
 * Represent all the infos return by git after a pull request
 *
 * @param title       the title name of the pull request
 * @param description the desciption of the pull request
 * @param author      who created or did this event
 * @param url         the link to the pull request
 */
@Serdeable
public record WebhookInfoDto(@NotNull String title, @NotNull String description, @NotNull String author,
                             @NotNull String url) {
}
