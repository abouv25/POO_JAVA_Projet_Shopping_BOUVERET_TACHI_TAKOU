package DAO;

import modele.*;
import java.sql.*;
import java.util.*;

public class FactureDAO {

    // Méthode pour ajouter une facture
    public boolean ajouterFacture(Facture f) {
        String sqlFacture = "INSERT INTO facture (id_utilisateur, date_facture, montant_total) VALUES (?, ?, ?)";
        String sqlLigneFacture = "INSERT INTO ligne_facture (id_facture, id_produit, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnexionBD.getConnection()) {
            conn.setAutoCommit(false); // Démarre la transaction

            try (PreparedStatement stmtFacture = conn.prepareStatement(sqlFacture, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stmtLigne = conn.prepareStatement(sqlLigneFacture)) {

                // 1. Insérer la facture
                stmtFacture.setInt(1, f.getClient().getId());
                stmtFacture.setDate(2, java.sql.Date.valueOf(f.getDate()));  // Utilisation de java.sql.Date
                stmtFacture.setDouble(3, f.getMontantTotal());
                stmtFacture.executeUpdate();

                // Récupérer l'ID généré
                try (ResultSet generatedKeys = stmtFacture.getGeneratedKeys()) {
                    if (!generatedKeys.next()) throw new SQLException("Échec de la génération de l'ID de facture.");
                    int idFacture = generatedKeys.getInt(1);
                    f.setId(idFacture);
                }

                // 2. Insérer les lignes de facture
                for (LignePanier ligne : f.getLignes()) {
                    stmtLigne.setInt(1, f.getId());
                    stmtLigne.setInt(2, ligne.getProduit().getId());
                    stmtLigne.setInt(3, ligne.getQuantite());
                    stmtLigne.setDouble(4, ligne.getProduit().getPrix());
                    stmtLigne.addBatch();
                }
                stmtLigne.executeBatch();

                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Erreur lors de l'enregistrement de la facture : " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            return false;
        }
    }

    // Méthode pour lister les factures pour un utilisateur donné
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
                        rs.getDouble("montant_total")
                );
                factures.add(f);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des factures : " + e.getMessage());
        }
        return factures;
    }

    // Méthode pour obtenir les lignes de la facture
    public List<LignePanier> listerLignesParFacture(int idFacture) {
        List<LignePanier> lignes = new ArrayList<>();
        String sql = "SELECT lf.*, p.* FROM ligne_facture lf JOIN produit p ON lf.id_produit = p.id WHERE lf.id_facture = ?";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idFacture);
            ResultSet rs = stmt.executeQuery();

            ProduitDAO produitDAO = new ProduitDAO();

            while (rs.next()) {
                Produit p = produitDAO.resultSetToProduit(rs);
                LignePanier ligne = new LignePanier(rs.getInt("id_produit"), p, rs.getInt("quantite"));
                lignes.add(ligne);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des lignes de facture : " + e.getMessage());
        }
        return lignes;
    }

    // Méthode pour obtenir les factures d'une année spécifique
    public List<Facture> getFacturesByYear(int year) {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture WHERE YEAR(date_facture) = ? ORDER BY date_facture DESC";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Récupérer les informations de l'utilisateur
                Utilisateur utilisateur = new Utilisateur(rs.getInt("id_utilisateur"), "Nom Client", "email@example.com","motdepasse", true ); // Remplacer par une méthode correcte pour récupérer l'utilisateur

                // Créer un objet Facture avec remise par défaut à 0.0
                Facture f = new Facture(
                        rs.getInt("id"),
                        utilisateur, // L'utilisateur réel à remplacer ici
                        rs.getDate("date_facture").toLocalDate(),
                        rs.getDouble("montant_total")
                );
                f.setRemisePourcent(0.0);  // Remise par défaut (si nécessaire)
                factures.add(f);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des factures : " + e.getMessage());
        }

        return factures;
    }

    // Méthode pour obtenir les statistiques des ventes par produit
    public Map<Produit, Integer> getVentesParProduit(List<Facture> factures) {
        Map<Produit, Integer> ventesParProduit = new HashMap<>();

        for (Facture facture : factures) {
            for (LignePanier ligne : facture.getLignes()) {
                Produit produit = ligne.getProduit();
                ventesParProduit.merge(produit, ligne.getQuantite(), Integer::sum);
            }
        }

        return ventesParProduit;
    }

    // Méthode pour obtenir les statistiques des ventes par client
    public Map<String, Double> getVentesParClient(List<Facture> factures) {
        Map<String, Double> ventesParClient = new HashMap<>();

        for (Facture facture : factures) {
            String clientName = facture.getClient().getNom();
            ventesParClient.merge(clientName, facture.getMontantTotal(), Double::sum);
        }

        return ventesParClient;
    }
}
