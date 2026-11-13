package fr.uge.analysis.model;

import jakarta.validation.constraints.NotNull;

public record Diagnostic(
        @NotNull String file,
        int line,
        @NotNull Severity severity,
        @NotNull String message) {
}
