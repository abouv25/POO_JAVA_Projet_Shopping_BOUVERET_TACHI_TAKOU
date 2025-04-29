package DAO;

import modele.*;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class FactureDAO {

    public int ajouterFacture(Facture f) {
        String sqlFacture = "INSERT INTO facture (utilisateur_id, date_facture, montant_total) VALUES (?, ?, ?)";
        String sqlLigneFacture = "INSERT INTO ligne_facture (facture_id, produit_id, quantite, prix_unitaire) VALUES (?, ?, ?, ?)";
        int idFacture = -1;

        try (Connection conn = ConnexionBD.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtFacture = conn.prepareStatement(sqlFacture, Statement.RETURN_GENERATED_KEYS)) {
                stmtFacture.setInt(1, f.getClient().getId());
                stmtFacture.setDate(2, Date.valueOf(f.getDate()));
                stmtFacture.setDouble(3, f.getMontantTotal());
                stmtFacture.executeUpdate();

                ResultSet generatedKeys = stmtFacture.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idFacture = generatedKeys.getInt(1);
                    f.setId(idFacture);

                    // ✅ Sécurité : on vérifie que les lignes sont bien présentes
                    if (f.getLignes() == null || f.getLignes().isEmpty()) {
                        throw new SQLException("La facture n’a pas de lignes à insérer.");
                    }

                    try (PreparedStatement stmtLigne = conn.prepareStatement(sqlLigneFacture)) {
                        for (LignePanier ligne : f.getLignes()) {
                            stmtLigne.setInt(1, idFacture);
                            stmtLigne.setInt(2, ligne.getIdProduit());
                            stmtLigne.setInt(3, ligne.getQuantite());
                            stmtLigne.setDouble(4, ligne.getPrix());
                            stmtLigne.addBatch();
                        }
                        stmtLigne.executeBatch();
                    }
                } else {
                    throw new SQLException("Aucune clé générée pour la facture.");
                }

                conn.commit();
                System.out.println("✅ Facture insérée avec succès : ID = " + idFacture);

            } catch (SQLException e) {
                conn.rollback();
                System.err.println("❌ Erreur lors de l'ajout de facture : " + e.getMessage());
                e.printStackTrace();
                idFacture = -1;
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur de connexion à la BDD : " + e.getMessage());
            e.printStackTrace();
            idFacture = -1;
        }

        return idFacture;
    }

    public List<Facture> listerFacturesPourUtilisateur(Utilisateur u) {
        List<Facture> factures = new ArrayList<>();
        String sql = "SELECT * FROM facture WHERE utilisateur_id = ? ORDER BY date_facture DESC";  // ✅ colonne corrigée

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, u.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                factures.add(new Facture(
                        rs.getInt("id"),
                        u,
                        rs.getDate("date_facture").toLocalDate(),
                        rs.getDouble("montant_total"),
                        u.isClientFidele() ? 10.0 : 0.0
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération factures utilisateur : " + e.getMessage());
            e.printStackTrace();
        }

        return factures;
    }

    public List<LignePanier> listerLignesParFacture(int idFacture) {
        List<LignePanier> lignes = new ArrayList<>();
        String sql = """
            SELECT p.id, p.nom, p.prix, lf.quantite
            FROM ligne_facture lf
            JOIN produit p ON p.id = lf.produit_id
            WHERE lf.facture_id = ?
        """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFacture);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lignes.add(new LignePanier(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getDouble("prix"),
                        rs.getInt("quantite")
                ));
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération lignes facture : " + e.getMessage());
            e.printStackTrace();
        }

        return lignes;
    }

    // === STATISTIQUES GLOBALES ===

    public Map<String, Double> totalVentesParDate() {
        Map<String, Double> map = new TreeMap<>();
        String sql = "SELECT DATE(date_facture) AS jour, SUM(montant_total) AS totalJour FROM facture GROUP BY jour ORDER BY jour";

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                map.put(rs.getString("jour"), rs.getDouble("totalJour"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur stats par date : " + e.getMessage());
        }

        return map;
    }

    // === STATISTIQUES FILTRÉES (ANNEE + MOIS) ===

    public Map<String, Double> totalVentesParDate(int annee, int mois) {
        Map<String, Double> map = new TreeMap<>();
        String sql = """
            SELECT DATE(date_facture) AS jour, SUM(montant_total) AS totalJour
            FROM facture
            WHERE YEAR(date_facture) = ? AND MONTH(date_facture) = ?
            GROUP BY jour ORDER BY jour
        """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, annee);
            stmt.setInt(2, mois);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("jour"), rs.getDouble("totalJour"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur stats date (filtrée) : " + e.getMessage());
        }

        return map;
    }

    public Map<String, Double> totalVentesParUtilisateur(int annee, int mois) {
        Map<String, Double> map = new TreeMap<>();
        String sql = """
            SELECT u.nom, SUM(f.montant_total) AS totalVentes
            FROM facture f
            JOIN utilisateur u ON f.utilisateur_id = u.id
            WHERE YEAR(f.date_facture) = ? AND MONTH(f.date_facture) = ?
            GROUP BY u.nom ORDER BY totalVentes DESC
        """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, annee);
            stmt.setInt(2, mois);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("nom"), rs.getDouble("totalVentes"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur stats utilisateur (filtrée) : " + e.getMessage());
        }

        return map;
    }

    public List<Facture> listerToutesLesFactures() {
        List<Facture> factures = new ArrayList<>();
        String sql = """
        SELECT f.*, u.nom, u.email, u.is_admin, u.is_fidele
        FROM facture f
        JOIN utilisateur u ON f.utilisateur_id = u.id
        ORDER BY f.date_facture DESC
    """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Utilisateur u = new Utilisateur(
                        rs.getInt("id_utilisateur"),
                        rs.getString("nom"),
                        rs.getString("email"),
                        "", // mot de passe non utilisé ici
                        rs.getBoolean("is_admin"),
                        rs.getBoolean("is_fidele")
                );
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
            System.err.println("Erreur listerToutesLesFactures : " + e.getMessage());
        }

        return factures;
    }


    public Map<String, Double> totalVentesParProduit(int annee, int mois) {
        Map<String, Double> map = new TreeMap<>();
        String sql = """
            SELECT p.nom, SUM(lf.quantite * lf.prix_unitaire) AS totalVente
            FROM ligne_facture lf
            JOIN produit p ON p.id = lf.produit_id
            JOIN facture f ON f.id = lf.facture_id
            WHERE YEAR(f.date_facture) = ? AND MONTH(f.date_facture) = ?
            GROUP BY p.nom ORDER BY totalVente DESC
        """;

        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, annee);
            stmt.setInt(2, mois);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                map.put(rs.getString("nom"), rs.getDouble("totalVente"));
            }

        } catch (SQLException e) {
            System.err.println("Erreur stats produit (filtrée) : " + e.getMessage());
        }

        return map;
    }
}
