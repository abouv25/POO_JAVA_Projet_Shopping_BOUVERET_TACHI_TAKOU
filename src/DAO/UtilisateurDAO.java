package DAO;

import modele.Utilisateur;

import java.sql.*;

/**
 * DAO pour gérer les opérations sur les utilisateurs.
 */
public class UtilisateurDAO {

    // Ajouter un utilisateur dans la base
    public boolean ajouterUtilisateur(Utilisateur u) {
        String sql = "INSERT INTO utilisateur (nom, email, motDePasse, clientFidele) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getMotDePasse());
            stmt.setBoolean(4, u.isClientFidele());

            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // Retrouver un utilisateur par email et mot de passe (connexion)
    public Utilisateur trouverParEmailEtMotDePasse(String email, String motDePasse) {
        String sql = "SELECT * FROM utilisateur WHERE email = ? AND motDePasse = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getBoolean("clientFidele")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur : " + e.getMessage());
        }

        return null;
    }
}
