package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueAccueil extends JPanel {

    private MainWindow mainWindow;
    private JButton boutonCatalogue;
    private JButton boutonConnexion;
    private JButton boutonHistorique;
    private JButton boutonPanier;
    private JButton boutonDeconnexion;
    private JLabel bienvenueLabel;

    public VueAccueil(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        // --- Titre ou logo ---
        JLabel titre = new JLabel("🛒 Bienvenue sur Shopping B-T-T", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 22));
        add(titre, BorderLayout.NORTH);

        // --- Contenu central ---
        JPanel centre = new JPanel(new GridLayout(5, 1, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        bienvenueLabel = new JLabel("", SwingConstants.CENTER);
        centre.add(bienvenueLabel);

        boutonCatalogue = new JButton("🛍️ Catalogue Produits");
        boutonConnexion = new JButton("🔑 Connexion");
        boutonHistorique = new JButton("📄 Historique de Factures");
        boutonPanier = new JButton("🛒 Mon Panier");
        boutonDeconnexion = new JButton("🚪 Déconnexion");

        centre.add(boutonCatalogue);
        centre.add(boutonConnexion);
        centre.add(boutonPanier);
        centre.add(boutonHistorique);
        centre.add(boutonDeconnexion);

        add(centre, BorderLayout.CENTER);

        // --- Actions des boutons ---
        boutonCatalogue.addActionListener(e -> mainWindow.switchTo("catalogue"));
        boutonConnexion.addActionListener(e -> mainWindow.switchTo("connexion"));
        boutonPanier.addActionListener(e -> mainWindow.switchTo("panier"));
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

        // --- Mise à jour dynamique ---
        mettreAJourAffichage();
    }

    public void mettreAJourAffichage() {
        Utilisateur u = mainWindow.getUtilisateurConnecte();

        boolean connecte = (u != null);
        bienvenueLabel.setText(connecte ? "Bonjour " + u.getNom() + " !" : "Bienvenue, invité !");

        boutonConnexion.setVisible(!connecte);
        boutonHistorique.setVisible(connecte);
        boutonPanier.setVisible(connecte);
        boutonDeconnexion.setVisible(connecte);
    }
}
