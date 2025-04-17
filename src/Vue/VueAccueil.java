package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueAccueil extends JPanel {

    private MainWindow mainWindow;
    private JLabel bienvenueLabel;
    private JButton boutonCatalogue;
    private JButton boutonConnexion;
    private JButton boutonPanier;
    private JButton boutonHistorique;
    private JButton boutonDeconnexion;

    public VueAccueil(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // --- Logo réduit par 4 ---
        ImageIcon logo = new ImageIcon(getClass().getResource("logoBTTShopping.png"));
        Image imgReduite = logo.getImage().getScaledInstance(
                logo.getIconWidth() / 4,
                logo.getIconHeight() / 4,
                Image.SCALE_SMOOTH
        );
        JLabel logoLabel = new JLabel(new ImageIcon(imgReduite));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // --- Centre avec boutons + message ---
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUI.appliquerFondEtCadre(centre);

        bienvenueLabel = new JLabel("", SwingConstants.CENTER);
        bienvenueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StyleUI.styliserTitre(bienvenueLabel);
        centre.add(bienvenueLabel);
        centre.add(Box.createRigidArea(new Dimension(0, 20)));

        boutonCatalogue = new JButton("🛍️ Catalogue Produits");
        boutonConnexion = new JButton("🔑 Connexion");
        boutonPanier = new JButton("🛒 Mon Panier");
        boutonHistorique = new JButton("📄 Historique de Factures");
        boutonDeconnexion = new JButton("🚪 Déconnexion");

        for (JButton btn : new JButton[]{boutonCatalogue, boutonConnexion, boutonPanier, boutonHistorique, boutonDeconnexion}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            StyleUI.styliserBouton(btn);
            centre.add(btn);
            centre.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(centre, BorderLayout.CENTER);

        // --- Listeners des boutons ---
        boutonCatalogue.addActionListener(e -> mainWindow.switchTo("catalogue"));

        boutonConnexion.addActionListener(e -> mainWindow.switchTo("connexion"));

        boutonPanier.addActionListener(e -> {
            Utilisateur u = mainWindow.getUtilisateurConnecte();
            if (u != null) {
                mainWindow.switchTo("panier");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez vous connecter pour accéder au panier.");
                mainWindow.switchTo("connexion");
            }
        });

        boutonHistorique.addActionListener(e -> {
            Utilisateur u = mainWindow.getUtilisateurConnecte();
            VueHistoriqueFactures vueHisto = new VueHistoriqueFactures(u, mainWindow);
            mainWindow.ajouterVue("historique", vueHisto);
            mainWindow.switchTo("historique");
        });

        boutonDeconnexion.addActionListener(e -> {
            mainWindow.setUtilisateurConnecte(null);
            JOptionPane.showMessageDialog(mainWindow, "Déconnexion réussie.");
            mainWindow.switchTo("accueil");
        });

        mettreAJourAffichage();
    }

    public void mettreAJourAffichage() {
        Utilisateur u = mainWindow.getUtilisateurConnecte();
        boolean connecte = (u != null);
        bienvenueLabel.setText(connecte ? "Bonjour " + u.getPrenom() + " " + u.getNom() + " !" : "Bienvenue, invité !");

        boutonConnexion.setVisible(!connecte);
        boutonHistorique.setVisible(connecte);
        boutonPanier.setVisible(true);
        boutonDeconnexion.setVisible(connecte);
    }
}
