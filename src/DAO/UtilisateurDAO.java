package DAO;

import modele.Utilisateur;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // ✅ Ajouter un utilisateur
    public boolean ajouterUtilisateur(Utilisateur u) {
        String sql = "INSERT INTO utilisateur (nom, prenom, email, motDePasse, estAdmin, clientFidele) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setBoolean(5, u.isEstAdmin());
            stmt.setBoolean(6, u.isClientFidele());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // ✅ Vérifier connexion : retourne un objet complet
    public Utilisateur verifierConnexion(String email, String motDePasse) {
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
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getBoolean("estAdmin"),
                        rs.getBoolean("clientFidele")
                );
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion : " + e.getMessage());
        }

        return null;
    }

    // ✅ Modifier utilisateur
    public boolean modifierUtilisateur(Utilisateur u) {
        String sql = "UPDATE utilisateur SET nom = ?, prenom = ?, email = ?, motDePasse = ?, estAdmin = ?, clientFidele = ? WHERE id = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNom());
            stmt.setString(2, u.getPrenom());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getMotDePasse());
            stmt.setBoolean(5, u.isEstAdmin());
            stmt.setBoolean(6, u.isClientFidele());
            stmt.setInt(7, u.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    // ✅ Supprimer utilisateur
    public boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateur WHERE id = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression : " + e.getMessage());
            return false;
        }
    }

    // ✅ Lister tous les utilisateurs
    public List<Utilisateur> listerUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateur";

        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("motDePasse"),
                        rs.getBoolean("estAdmin"),
                        rs.getBoolean("clientFidele")
                );
                liste.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du listing : " + e.getMessage());
        }

        return liste;
    }

    // ✅ Modifier le mot de passe à partir de l'email
    public boolean modifierMotDePasse(String email, String nouveauMDP) {
        String sql = "UPDATE utilisateur SET motDePasse = ? WHERE email = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nouveauMDP);
            stmt.setString(2, email);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Erreur modification mot de passe : " + e.getMessage());
            return false;
        }
    }
}
