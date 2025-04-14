package Vue;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private boolean isUserAuthenticated = false;  // Variable d'état d'authentification

    // Constructeur de la fenêtre principale
    public MainWindow() {
        setTitle("Application Shopping");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Création des vues pour chaque section
        ConnexionView connexionView = new ConnexionView(this);
        AccueilView accueilView = new AccueilView(this);
        ProduitsView produitsView = new ProduitsView(this);
        FacturesView facturesView = new FacturesView(this);
        VuePanier vuePanier = new VuePanier(this); // Vue du panier

        // Ajout des vues dans le CardLayout
        mainPanel.add(connexionView, "connexion");
        mainPanel.add(accueilView, "accueil");
        mainPanel.add(produitsView, "produits");
        mainPanel.add(facturesView, "factures");
        mainPanel.add(vuePanier, "panier");  // Ajout de la vue du panier

        // Ajout du panel principal
        setContentPane(mainPanel);

        // Affiche l’écran de connexion au lancement
        cardLayout.show(mainPanel, "connexion");

        // Affichage de la fenêtre
        setVisible(true);
    }

    // Méthode pour changer de vue
    public void switchTo(String viewName) {
        // Vérification de l'authentification avant d'accéder aux vues principales
        if (viewName.equals("accueil") && !isUserAuthenticated) {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter d'abord.");
            cardLayout.show(mainPanel, "connexion");  // Retour à la page de connexion
            return;
        }
        cardLayout.show(mainPanel, viewName);  // Changer de vue si l'utilisateur est authentifié
    }

    // Méthode pour mettre à jour l'état de l'utilisateur (authentification réussie)
    public void setUserAuthenticated(boolean isAuthenticated) {
        this.isUserAuthenticated = isAuthenticated;
    }

    // Getter pour le panel principal si besoin
    public JPanel getMainPanel() {
        return mainPanel;
    }

    // Méthode main pour lancer l'application
    public static void main(String[] args) {
        // Exécution de l'application dans le thread de l'interface graphique
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();  // Lancer la fenêtre principale
            }
        });
    }
}
