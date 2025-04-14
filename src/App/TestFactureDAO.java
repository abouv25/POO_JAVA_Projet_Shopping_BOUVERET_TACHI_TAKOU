package App;

import DAO.FactureDAO;
import modele.Facture;
import modele.LignePanier;
import modele.Produit;
import modele.Utilisateur;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TestFactureDAO {
    public static void main(String[] args) {
        // Création d'un utilisateur de test
        Utilisateur utilisateur = new Utilisateur(1, "John Doe", "john@example.com", "password", true);

        // Création d'un produit de test
        Produit produit1 = new Produit(1, "Produit 1", 15.0, 100);
        Produit produit2 = new Produit(2, "Produit 2", 20.0, 50);

        // Création des lignes de facture avec des objets Produit
        LignePanier ligne1 = new LignePanier(produit1.getId(), produit1, 2); // Ligne pour produit1
        LignePanier ligne2 = new LignePanier(produit2.getId(), produit2, 3); // Ligne pour produit2
        // Création de la facture
        Facture facture = new Facture(utilisateur, List.of(ligne1, ligne2));

        // Calcul du total de la facture
        facture.setMontantTotal(facture.calculerTotal());

        // Essayer d'ajouter la facture
        FactureDAO factureDAO = new FactureDAO();
        if (factureDAO.ajouterFacture(facture)) {
            System.out.println("Facture ajoutée avec succès !");
        } else {
            System.out.println("Échec de l'ajout de la facture.");
        }

        // Tester les statistiques de vente par produit
        List<Facture> factures = factureDAO.listerFacturesPourUtilisateur(utilisateur);
        Map<Produit, Integer> ventesParProduit = factureDAO.getVentesParProduit(factures);
        System.out.println("Statistiques des ventes par produit : ");
        for (Map.Entry<Produit, Integer> entry : ventesParProduit.entrySet()) {
            System.out.println(entry.getKey().getNom() + " : " + entry.getValue() + " unités vendues");
        }

        // Tester les statistiques de vente par client
        Map<String, Double> ventesParClient = factureDAO.getVentesParClient(factures);
        System.out.println("Statistiques des ventes par client : ");
        for (Map.Entry<String, Double> entry : ventesParClient.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " €");
        }
    }
}
