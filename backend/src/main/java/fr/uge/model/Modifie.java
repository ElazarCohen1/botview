package fr.uge.model;

import fr.uge.enums.Status;

import java.time.Instant;
import java.util.Objects;

public record Modifie(int pullId, Instant dateModification, Status status, int fichierId) {
    public Modifie {
        Objects.requireNonNull(status);
        Objects.requireNonNull(dateModification);
    }
}
