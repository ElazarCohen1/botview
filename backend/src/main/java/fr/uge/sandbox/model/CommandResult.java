package fr.uge.sandbox.model;

import jakarta.validation.constraints.NotNull;

public record CommandResult(int exitCode, @NotNull String stdout, @NotNull String stderr, boolean timeout) {
}
