package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ComposantsUI {

    public static JPanel creerBarreSuperieure(MainWindow mainWindow) {
        JPanel barre = new JPanel(new BorderLayout());
        barre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        barre.setBackground(Color.LIGHT_GRAY);

        // ✅ Logo cliquable
        ImageIcon logo = null;
        try {
            logo = new ImageIcon(ComposantsUI.class.getResource("/Vue/logoBTTShopping.png"));
        } catch (Exception e) {
            System.err.println("Erreur chargement du logo : " + e.getMessage());
            logo = new ImageIcon(); // logo vide si erreur
        }

        JLabel logoLabel = new JLabel(logo);
        logoLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainWindow.switchTo("accueil");
            }
        });
        barre.add(logoLabel, BorderLayout.WEST);

        // ✅ Panneau à droite : utilisateur + bouton panier
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        droite.setOpaque(false);

        JLabel infoUtilisateur = new JLabel();
        infoUtilisateur.setFont(new Font("SansSerif", Font.PLAIN, 14));

        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u != null) {
            infoUtilisateur.setText("👤 " + u.getNom());
        } else {
            infoUtilisateur.setText("🔐 S’identifier");
            infoUtilisateur.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            infoUtilisateur.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    mainWindow.switchTo("connexion");
                }
            });
        }

        // ✅ Bouton Voir Panier
        JButton boutonPanier = new JButton("🛍️ Voir Panier");
        boutonPanier.setFocusPainted(false);
        boutonPanier.setFont(new Font("SansSerif", Font.PLAIN, 13));
        boutonPanier.addActionListener(e -> mainWindow.switchTo("panier"));

        droite.add(infoUtilisateur);
        droite.add(boutonPanier);

        barre.add(droite, BorderLayout.EAST);

        return barre;
    }
}
