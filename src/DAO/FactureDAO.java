package DAO;

import modele.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FactureDAO {

    public boolean ajouterFacture(Facture f) {
        String sqlFacture = "INSERT INTO facture (id_utilisateur, date_facture, montant_total) VALUES (?, ?, ?)";
        String sqlLigneFacture = "INSERT INTO ligne_facture (id_facture, id_produit, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement stmtFacture = null;
        PreparedStatement stmtLigne = null;
        ResultSet generatedKeys = null;

        try {
            conn = ConnexionBD.getConnection();
            conn.setAutoCommit(false); // Démarre la transaction

            // 1. Insérer la facture
            stmtFacture = conn.prepareStatement(sqlFacture, Statement.RETURN_GENERATED_KEYS);
            stmtFacture.setInt(1, f.getClient().getId());
            stmtFacture.setDate(2, Date.valueOf(f.getDate()));
            stmtFacture.setDouble(3, f.getMontantTotal());
            stmtFacture.executeUpdate();

            generatedKeys = stmtFacture.getGeneratedKeys();
            if (!generatedKeys.next()) throw new SQLException("Échec de la génération de l’ID de facture.");
            int idFacture = generatedKeys.getInt(1);
            f.setId(idFacture);

            // 2. Insérer les lignes de facture
            stmtLigne = conn.prepareStatement(sqlLigneFacture);
            for (LignePanier ligne : f.getLignes()) {
                stmtLigne.setInt(1, idFacture);
                stmtLigne.setInt(2, ligne.getIdProduit());
                stmtLigne.setInt(3, ligne.getQuantite());
                stmtLigne.setDouble(4, ligne.getPrix());
                stmtLigne.addBatch();
            }
            stmtLigne.executeBatch();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de la facture : " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Erreur lors du rollback : " + ex.getMessage());
            }
            return false;

        } finally {
            try {
                if (stmtFacture != null) stmtFacture.close();
                if (stmtLigne != null) stmtLigne.close();
                if (generatedKeys != null) generatedKeys.close();
                if (conn != null) conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture des ressources : " + e.getMessage());
            }
        }
    }

    public List<Facture> listerFacturesPourUtilisateur(Utilisateur u) {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture WHERE id_utilisateur = ? ORDER BY date_facture DESC";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Facture f = new Facture(
                        rs.getInt("id"),
                        u,
                        rs.getDate("date_facture").toLocalDate(),
                        rs.getDouble("montant_total"),
                        u.isClientFidele() ? 10.0 : 0.0
                );
                factures.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des factures : " + e.getMessage());
        }

        return factures;
    }

    public List<LignePanier> listerLignesParFacture(int idFacture) {
        List<LignePanier> lignes = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nom, p.prix, lf.quantite
            FROM ligne_facture lf
            JOIN produit p ON p.id = lf.id_produit
            WHERE lf.id_facture = ?
        """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFacture);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LignePanier ligne = new LignePanier(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite")
                );
                lignes.add(ligne);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des lignes de la facture : " + e.getMessage());
        }

        return lignes;
    }
}
