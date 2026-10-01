package fr.uge.model;

import fr.uge.enums.Statut;

import java.util.Objects;

public record LigneModifie(int ligneId, int fichierId, int pullId, Statut statut, String commentaire, String ancienne, String nouvelle, int numeroLigne) {
    public  LigneModifie{
        Objects.requireNonNull(statut);
        Objects.requireNonNull(commentaire);
        Objects.requireNonNull(ancienne);
        Objects.requireNonNull(nouvelle);
    }
}
