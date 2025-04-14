package App;

import modele.LignePanier;
import modele.Produit;
import modele.Utilisateur;
import Vue.VueFacture;

import javax.swing.*;
import java.util.List;

public class MainFactureTest {
    public static void main(String[] args) {
        // Simuler un utilisateur existant (id = 1)
        Utilisateur utilisateur = new Utilisateur(1, "Antoine", "a@mail.com", "123", true);

        // Simuler un panier : 2 produits
        Produit produit1 = new Produit(1, "Écouteurs", 25.00, 100); // Création du produit "Écouteurs"
        Produit produit2 = new Produit(2, "Chargeur USB-C", 15.00, 50); // Création du produit "Chargeur USB-C"

        List<LignePanier> panierSimule = List.of(
                new LignePanier(produit1.getId(), produit1, 2), // Ligne pour "Écouteurs"
                new LignePanier(produit2.getId(), produit2, 1)  // Ligne pour "Chargeur USB-C"
        );

        // Lancer la fenêtre de facture
        SwingUtilities.invokeLater(() -> new VueFacture(utilisateur, panierSimule).setVisible(true));
    }
}
