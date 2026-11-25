package fr.uge.review.model;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ParsedFile(@NotNull String path, @NotNull ChangeStatus status,@NotNull List<ParsedLine> lines) {
}
