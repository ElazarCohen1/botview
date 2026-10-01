package fr.uge;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {

        DatabaseInitializer db = new DatabaseInitializer("jdbc:duckdb:./data/botview.duckdb");
        db.init();

        Micronaut.run(Application.class, args);
    }
}