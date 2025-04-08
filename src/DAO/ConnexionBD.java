package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire pour gérer la connexion à la base de données via JDBC.
 */
public class ConnexionBD {
    private static final String URL = "jdbc:mysql://localhost:3308/shopping"; // ✅ port mis à jour
    private static final String UTILISATEUR = "root"; // adapte si besoin
    private static final String MOT_DE_PASSE = "";    // adapte si besoin

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver JDBC : " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
    }
}
