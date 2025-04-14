package Vue;

import javax.swing.*;
import java.awt.*;

/**
 * Vue d'accueil de l'application de shopping.
 */
public class AccueilView extends JPanel {

    public AccueilView(MainWindow mainWindow) {
        setLayout(new BorderLayout());

        // Titre de bienvenue
        JLabel label = new JLabel("Bienvenue dans l'application de Shopping", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Bouton pour accéder à la vue des produits
        JButton btnGoToProduits = new JButton("Voir les produits");
        btnGoToProduits.addActionListener(e -> mainWindow.switchTo("produits"));
        add(btnGoToProduits, BorderLayout.SOUTH);

        // Bouton pour accéder au panier
        JButton btnGoToPanier = new JButton("Voir le panier");
        btnGoToPanier.addActionListener(e -> mainWindow.switchTo("panier")); // Bascule vers la vue du panier
        add(btnGoToPanier, BorderLayout.NORTH); // Ajout du bouton en haut de la vue
    }
}
