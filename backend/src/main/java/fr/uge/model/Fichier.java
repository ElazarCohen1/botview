package fr.uge.model;

import java.util.Objects;

public record Fichier(int fichierId, String name, String relativePath) {
    public  Fichier{
        Objects.requireNonNull(name);
        Objects.requireNonNull(relativePath);
    }
}
