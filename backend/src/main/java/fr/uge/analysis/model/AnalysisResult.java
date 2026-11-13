package fr.uge.analysis.model;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AnalysisResult(@NotNull String tool,
                             @NotNull AnalysisStatus status,
                             @NotNull List<Diagnostic> diagnostics,
                             @NotNull String errorMessage) {
}
