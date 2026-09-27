package fr.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    //private final String url = "jdbc:duckdb:./data/botview.duckdb";
    private final String url;

    public DatabaseInitializer(String url){
        this.url = url;
    }

    public  void init(){
        try{
            Connection conn = DriverManager.getConnection(url);
            System.out.println("Connexion réussie !");

            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS CLIENT (num_id INTEGER PRIMARY KEY, nom VARCHAR, prenom VARCHAR, email VARCHAR) ");
            stmt.execute("CREATE TABLE IF NOT EXISTS REPO_GIT (rep_id INTEGER PRIMARY KEY, nom VARCHAR, url VARCHAR, plateforme VARCHAR, branche VARCHAR) ");
            stmt.execute("CREATE TABLE IF NOT EXISTS PULL_REQUEST (pull_id INTEGER PRIMARY KEY, commentaire VARCHAR, name VARCHAR, date date, rep_id INTEGER REFERENCES REPO_GIT(rep_id)) ");
            stmt.execute("CREATE TABLE IF NOT EXISTS FICHIER  (fichier_id INTEGER PRIMARY KEY, status VARCHAR, name VARCHAR, date_modification date, relative_path VARCHAR) ");
            stmt.execute("CREATE TABLE IF NOT EXISTS LIGNE_MODIFIE (ligne_id INTEGER PRIMARY KEY, status VARCHAR, commentaire VARCHAR,ancienne VARCHAR, nouvelle VARCHAR, numero_ligne INTEGER, fichier_id INTEGER REFERENCES FICHIER(fichier_id) )");
            stmt.execute("CREATE TABLE IF NOT EXISTS POSSEDE  (rep_id INTEGER REFERENCES REPO_GIT(rep_id), num_id INTEGER REFERENCES CLIENT(num_id), PRIMARY KEY(num_id, rep_id) ) ");
            stmt.execute("CREATE TABLE IF NOT EXISTS MODIFIE  ( pull_id INTEGER REFERENCES PULL_REQUEST(pull_id), fichier_id INTEGER REFERENCES FICHIER(fichier_id), PRIMARY KEY(fichier_id, pull_id) ) ");

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Probleme de connexion base de donnée : " + e.getMessage());
        }
    }
}
