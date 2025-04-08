package DAO;

import modele.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO pour gérer les opérations sur les produits.
 */
public class ProduitDAO {

    // Ajouter un produit à la base de données
    public boolean ajouterProduit(Produit produit) {
        String sql = "INSERT INTO produit (nom, prix, quantiteStock) VALUES (?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrix());
            stmt.setInt(3, produit.getQuantiteStock());

            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
            return false;
        }
    }

    // Récupérer tous les produits depuis la base de données
    public List<Produit> getTousLesProduits() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit";

        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Produit p = new Produit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantiteStock")
                );
                produits.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }

        return produits;
    }
}
