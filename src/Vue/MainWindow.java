package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Utilisateur utilisateurConnecte;

    // Pour garder la main sur les vues si besoin
    private Map<String, JPanel> vues = new HashMap<>();

    public MainWindow() {
        setTitle("Application Shopping");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Vue de connexion
        ConnexionView connexionView = new ConnexionView(this);
        mainPanel.add(connexionView, "connexion");
        vues.put("connexion", connexionView);

        // Tu ajouteras ici d'autres vues comme :
        // mainPanel.add(new VueCatalogue(this), "catalogue");

        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "connexion");

        setVisible(true);
    }

    // Naviguer vers une vue existante
    public void switchTo(String viewName) {
        if (!vues.containsKey(viewName)) {
            System.err.println("Vue inconnue : " + viewName);
        }
        cardLayout.show(mainPanel, viewName);
    }

    // Ajouter une nouvelle vue dynamiquement (optionnel)
    public void ajouterVue(String nom, JPanel vue) {
        mainPanel.add(vue, nom);
        vues.put(nom, vue);
    }

    // Stocker l’utilisateur connecté
    public void setUtilisateurConnecte(Utilisateur u) {
        this.utilisateurConnecte = u;
    }

    // Récupérer l’utilisateur connecté (si utile dans d’autres vues)
    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    // Accès à la map des vues (si besoin pour vérif)
    public Map<String, JPanel> getVues() {
        return vues;
    }
}
