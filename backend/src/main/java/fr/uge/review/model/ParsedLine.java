package fr.uge.review.model;

import jakarta.validation.constraints.NotNull;


public record ParsedLine(int lineNumber, @NotNull ChangeStatus status, String oldValue, String newValue) {
}
