package fr.uge;

import fr.db.DatabaseInitializer;
import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args)
    {
        DatabaseInitializer t = new DatabaseInitializer("jdbc:duckdb:./data/botview.duckdb");
        t.init();
        Micronaut.run(Application.class, args);
    }
}