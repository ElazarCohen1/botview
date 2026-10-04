package fr.uge.model;

import fr.uge.enums.Status;

import java.util.Objects;

public record LigneModifie(int ligneId, int fichierId, int pullId, Status statut, String commentaire, String ancienne,
                           String nouvelle, int numeroLigne) {
  public LigneModifie {
    Objects.requireNonNull(statut);
    Objects.requireNonNull(commentaire);
    Objects.requireNonNull(ancienne);
    Objects.requireNonNull(nouvelle);
  }
}
