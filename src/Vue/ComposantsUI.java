package Vue;

import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ComposantsUI {

    public static JPanel creerBarreSuperieure(MainWindow mainWindow) {
        JPanel barre = new JPanel(new BorderLayout());
        barre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        barre.setBackground(Color.WHITE);

        // --- Logo cliquable ---
        JButton logoButton;
        try {
            ImageIcon logo = new ImageIcon(ComposantsUI.class.getResource("/Vue/logoBTTShopping.png"));
            Image img = logo.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            logoButton = new JButton(new ImageIcon(img));
            logoButton.setBorderPainted(false);
            logoButton.setContentAreaFilled(false);
        } catch (Exception e) {
            logoButton = new JButton("BTT Shopping");
        }

        logoButton.addActionListener(e -> mainWindow.switchTo("accueil"));
        barre.add(logoButton, BorderLayout.WEST);

        // --- Centre : boutons admin ---
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centre.setOpaque(false);

        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u != null && u.isEstAdmin()) {
            JButton btnAdminProduits = new JButton("📦 Produits");
            JButton btnStats = new JButton("📊 Statistiques");
            JButton btnUtilisateurs = new JButton("👥 Utilisateurs");

            btnAdminProduits.addActionListener(e -> mainWindow.chargerVueAdmin());
            btnAdminProduits.addActionListener(e -> mainWindow.switchTo("admin"));

            btnStats.addActionListener(e -> mainWindow.switchTo("statistiques"));

            btnUtilisateurs.addActionListener(e -> mainWindow.chargerVueGestionUtilisateurs());
            btnUtilisateurs.addActionListener(e -> mainWindow.switchTo("gestionUtilisateurs"));

            for (JButton btn : new JButton[]{btnAdminProduits, btnStats, btnUtilisateurs}) {
                StyleUI.styliserBouton(btn);
                centre.add(btn);
            }
        }

        barre.add(centre, BorderLayout.CENTER);

        // --- Droite : bouton utilisateur + panier ---
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        droite.setOpaque(false);

        JButton boutonUtilisateur = new JButton();
        JButton boutonPanier = new JButton("🛒 Mon Panier");

        if (u != null) {
            boutonUtilisateur.setText("👤 Mon compte");
        } else {
            boutonUtilisateur.setText("🔑 S'identifier");
        }

        boutonUtilisateur.addActionListener(e -> {
            Utilisateur utilisateur = mainWindow.getUtilisateurConnecte();
            if (utilisateur != null) {
                mainWindow.switchTo("moncompte");
            } else {
                mainWindow.switchTo("connexion");
            }
        });

        boutonPanier.addActionListener(e -> {
            if (mainWindow.getUtilisateurConnecte() != null) {
                mainWindow.switchTo("panier");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez vous connecter pour accéder au panier.");
                mainWindow.switchTo("connexion");
            }
        });

        StyleUI.styliserBouton(boutonUtilisateur);
        StyleUI.styliserBouton(boutonPanier);

        droite.add(boutonUtilisateur);
        droite.add(boutonPanier);

        barre.add(droite, BorderLayout.EAST);

        return barre;
    }

    public static JPanel creerCarteProduit(Produit p, MainWindow mainWindow) {
        JPanel carte = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };

        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(160, 250));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        carte.setOpaque(false);

        // --- Image produit ---
        JLabel imageLabel = new JLabel();
        try {
            if (p.getImage() != null) {
                ImageIcon imgIcon = new ImageIcon(p.getImage());
                Image img = imgIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(img));
            } else {
                imageLabel.setText("Pas d'image");
            }
        } catch (Exception e) {
            imageLabel.setText("Image indisponible");
        }

        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(imageLabel);

        carte.add(Box.createVerticalStrut(10));

        // --- Infos produit ---
        JLabel nomLabel = new JLabel(p.getNom(), SwingConstants.CENTER);
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(nomLabel);

        JLabel prixLabel = new JLabel(String.format("%.2f €", p.getPrix()), SwingConstants.CENTER);
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prixLabel.setForeground(Color.DARK_GRAY);
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(prixLabel);

        carte.add(Box.createVerticalStrut(8));

        JButton boutonVoir = new JButton("Voir Produit");
        boutonVoir.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boutonVoir.setBackground(Color.WHITE);
        boutonVoir.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boutonVoir.setFocusPainted(false);
        boutonVoir.setAlignmentX(Component.CENTER_ALIGNMENT);

        boutonVoir.addActionListener(e -> {
            VueDetailProduit vue = new VueDetailProduit(mainWindow, p, true);
            vue.setVisible(true);
        });

        carte.add(boutonVoir);

        return carte;
    }
}
