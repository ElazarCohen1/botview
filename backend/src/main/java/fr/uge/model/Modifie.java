package fr.uge.model;

import fr.uge.enums.Statut;

import java.time.Instant;

public record Modifie(int pullId, Instant dateModification, Statut status, int fichierId) {

}
