package fr.uge.model;

import fr.uge.enums.Status;

import java.time.Instant;

public record Modifie(int pullId, Instant dateModification, Status status, int fichierId) {

}
