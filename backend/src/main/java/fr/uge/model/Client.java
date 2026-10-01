package fr.uge.model;

import java.util.Objects;

public record Client(int id, String nom, String prenom, String email) {
    public  Client{
        Objects.requireNonNull(nom);
        Objects.requireNonNull(prenom);
        Objects.requireNonNull(email);
    }
}
