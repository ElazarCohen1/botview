package fr.uge.git.model;

import jakarta.validation.constraints.NotNull;
import org.eclipse.jgit.transport.CredentialsProvider;

/**
 * the params for cloning the repository
 *
 * @param url         the full url of the repo
 * @param ref
 * @param credentials
 */
public record CloneParams(@NotNull String url, @NotNull String ref, @NotNull CredentialsProvider credentials) {
}
