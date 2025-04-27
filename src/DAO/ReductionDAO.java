package DAO;

import modele.Reduction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReductionDAO {

    // 🔗 Connexion à la base
    private Connection getConnection() throws SQLException {
        return ConnexionBD.getConnection();
    }

    // ✅ Ajouter une réduction
    public boolean ajouterReduction(Reduction reduction) {
        String sql = "INSERT INTO reduction (nom, pourcentage) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reduction.getNom());
            stmt.setDouble(2, reduction.getPourcentage());
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur ajout réduction : " + e.getMessage());
            return false;
        }
    }

    // ✅ Lister toutes les réductions
    public List<Reduction> listerReductions() {
        List<Reduction> liste = new ArrayList<>();
        String sql = "SELECT * FROM reduction";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Reduction r = new Reduction(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("pourcentage")
                );
                liste.add(r);
            }

        } catch (SQLException e) {
            System.err.println("Erreur listing réductions : " + e.getMessage());
        }

        return liste;
    }

    // ✅ Modifier une réduction existante
    public boolean modifierReduction(Reduction reduction) {
        String sql = "UPDATE reduction SET nom = ?, pourcentage = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, reduction.getNom());
            stmt.setDouble(2, reduction.getPourcentage());
            stmt.setInt(3, reduction.getId());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur modification réduction : " + e.getMessage());
            return false;
        }
    }

    // ✅ Supprimer une réduction
    public boolean supprimerReduction(int id) {
        String sql = "DELETE FROM reduction WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur suppression réduction : " + e.getMessage());
            return false;
        }
    }
}
