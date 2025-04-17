/*package App;

import modele.LignePanier;
import modele.Utilisateur;
import Vue.VueNouvelleFacture;

import javax.swing.*;
import java.util.List;

public class MainFactureTest {
    public static void main(String[] args) {
        // Simuler un utilisateur existant (id = 1)
        Utilisateur utilisateur = new Utilisateur(1, "Antoine", "a@mail.com", "123", true);

        // Simuler un panier : 2 produits
        List<LignePanier> panierSimule = List.of(
                new LignePanier(1, "Écouteurs", 25.00, 2),
                new LignePanier(2, "Chargeur USB-C", 15.00, 1)
        );

        // Lancer la fenêtre de facture
        SwingUtilities.invokeLater(() -> new VueNouvelleFacture(utilisateur,panierSimule).setVisible(true));
    }
}*/
