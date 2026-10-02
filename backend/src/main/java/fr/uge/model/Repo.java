package fr.uge.model;

import fr.uge.enums.Plateformes;

import java.util.Objects;

public record Repo(int id, String name, String url, Plateformes plateforme, String branche) {
  public Repo {
    Objects.requireNonNull(name);
    Objects.requireNonNull(url);
    Objects.requireNonNull(plateforme);
    Objects.requireNonNull(branche);
  }
}
