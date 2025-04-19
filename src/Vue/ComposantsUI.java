package Vue;

import modele.Produit;
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
                int confirm = JOptionPane.showConfirmDialog(mainWindow, "Retour à l'accueil ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    mainWindow.switchTo("accueil");
                }
            }
        });
        barre.add(logoLabel, BorderLayout.WEST);

        // ✅ Panneau au centre pour les boutons admin si admin connecté
        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centre.setOpaque(false);

        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u != null && u.isEstAdmin()) {
            JButton btnAdmin = new JButton("Produits");
            JButton btnStats = new JButton("Statistiques");
            JButton btnUtilisateurs = new JButton("Utilisateurs");

            btnAdmin.addActionListener(e -> mainWindow.switchTo("admin"));
            btnStats.addActionListener(e -> mainWindow.switchTo("statistiques"));
            btnUtilisateurs.addActionListener(e -> mainWindow.switchTo("gestionUtilisateurs"));

            centre.add(btnAdmin);
            centre.add(btnStats);
            centre.add(btnUtilisateurs);
        }

        barre.add(centre, BorderLayout.CENTER);

        // ✅ Panneau à droite : utilisateur + bouton panier
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        droite.setOpaque(false);

        JLabel infoUtilisateur = new JLabel();
        infoUtilisateur.setFont(new Font("SansSerif", Font.PLAIN, 14));

        if (u != null) {
            infoUtilisateur.setText("👤 " + u.getPrenom() + " " + u.getNom());
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

        // ✅ Image
        try {
            ImageIcon icon = new ImageIcon(p.getImage());
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(img));
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            carte.add(imageLabel);
        } catch (Exception e) {
            JLabel imageLabel = new JLabel("Image indisponible");
            imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            carte.add(imageLabel);
        }

        carte.add(Box.createVerticalStrut(10));

        // ✅ Nom
        JLabel nomLabel = new JLabel(p.getNom(), SwingConstants.CENTER);
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(nomLabel);

        // ✅ Prix
        JLabel prixLabel = new JLabel(String.format("%.2f €", p.getPrix()), SwingConstants.CENTER);
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prixLabel.setForeground(Color.DARK_GRAY);
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(prixLabel);

        carte.add(Box.createVerticalStrut(8));

        // ✅ Bouton Voir le produit (mode consultation seule)
        JButton boutonVoir = new JButton("Voir le produit");
        boutonVoir.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boutonVoir.setBackground(Color.WHITE);
        boutonVoir.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        boutonVoir.setFocusPainted(false);
        boutonVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonVoir.addActionListener(e -> {
            VueDetailProduit vue = new VueDetailProduit(p, true); // 👈 consultation seule
            vue.setVisible(true);
        });

        carte.add(boutonVoir);
        return carte;
    }

}
