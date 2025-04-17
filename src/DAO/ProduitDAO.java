package DAO;

import modele.Produit;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitDAO {

    // Ajouter un produit avec image
    public boolean ajouterProduit(Produit produit) {
        String sql = "INSERT INTO produit (nom, prix, quantiteStock, image) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrix());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setString(4, produit.getImage()); // ✅ image

            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du produit : " + e.getMessage());
            return false;
        }
    }
    // Récupérer un produit complet par ID
    public Produit getProduitParId(int id) {
        String sql = "SELECT * FROM produit WHERE id = ?";
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Produit(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getString("image")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur getProduitParId : " + e.getMessage());
        }
        return null;
    }


    // Lister tous les produits
    public List<Produit> listerProduits() {
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
                        rs.getInt("quantiteStock"),
                        rs.getString("image") // ✅ image chargée
                );
                produits.add(p);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des produits : " + e.getMessage());
        }

        return produits;
    }

    // Modifier un produit (avec image)
    public boolean modifierProduit(Produit produit) {
        String sql = "UPDATE produit SET nom = ?, prix = ?, quantiteStock = ?, image = ? WHERE id = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, produit.getNom());
            stmt.setDouble(2, produit.getPrix());
            stmt.setInt(3, produit.getQuantiteStock());
            stmt.setString(4, produit.getImage()); // ✅ image
            stmt.setInt(5, produit.getId());

            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du produit : " + e.getMessage());
            return false;
        }
    }

    // Supprimer un produit
    public boolean supprimerProduit(int idProduit) {
        String sql = "DELETE FROM produit WHERE id = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idProduit);
            int lignes = stmt.executeUpdate();
            return lignes > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du produit : " + e.getMessage());
            return false;
        }
    }
}
