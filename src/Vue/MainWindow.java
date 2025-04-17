package Vue;

import modele.Panier;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Utilisateur utilisateurConnecte;
    private Panier panier = new Panier();

    private Map<String, JPanel> vues = new HashMap<>();

    public MainWindow() {
        setTitle("Application Shopping");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Vues accessibles dès le démarrage
        ajouterVue("connexion", new ConnexionView(this));
        ajouterVue("accueil", new VueAccueil(this));
        ajouterVue("panier", new VuePanier(this));
        ajouterVue("inscription", new VueInscription(this));
        ajouterVue("accueilClient", new VueAccueil(this));
        ajouterVue("accueilAdmin", new VueAccueilAdmin(this));
        ajouterVue("historique", new VueHistoriqueCommandes(this));
        ajouterVue("stats", new VueStatistiques(this));


        // Préchargement du catalogue
        chargerCatalogue();

        // Affichage de la première vue
        setContentPane(mainPanel);
        cardLayout.show(mainPanel, "accueil");

        setVisible(true);
    }
    public void chargerVueGestionUtilisateurs() {
        if (!vues.containsKey("gestionUtilisateurs")) {
            VueGestionUtilisateurs vue = new VueGestionUtilisateurs(this);
            ajouterVue("gestionUtilisateurs", vue);
        }
    }


    // Ajouter une vue dynamiquement
    public void ajouterVue(String nom, JPanel vue) {
        mainPanel.add(vue, nom);
        vues.put(nom, vue);
    }

    // Supprimer une vue existante
    public void supprimerVue(String nom) {
        Component comp = vues.get(nom);
        if (comp != null) {
            mainPanel.remove(comp);
            vues.remove(nom);
        }
    }

    // Naviguer vers une vue
    public void switchTo(String viewName) {
        if (!vues.containsKey(viewName)) {
            System.err.println("Vue inconnue : " + viewName);
            return;
        }

        if ("accueil".equals(viewName)) {
            VueAccueil accueil = (VueAccueil) vues.get("accueil");
            accueil.mettreAJourAffichage();
        }

        if ("panier".equals(viewName)) {
            VuePanier vp = (VuePanier) vues.get("panier");
            vp.rafraichir();
        }

        if ("catalogue".equals(viewName)) {
            chargerCatalogue(); // sécurise l’accès
        }

        cardLayout.show(mainPanel, viewName);
    }

    public Panier getPanier() {
        return panier;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public Map<String, JPanel> getVues() {
        return vues;
    }

    // Chargement sécurisé et dynamique du catalogue
    public void chargerCatalogue() {
        if (!vues.containsKey("catalogue")) {
            VueProduits vueProduits = new VueProduits(this, utilisateurConnecte);
            ajouterVue("catalogue", vueProduits);
        }
    }

    // Chargement sécurisé de la vue admin
    public void chargerVueAdmin() {
        if (!vues.containsKey("admin")) {
            VueAdmin adminView = new VueAdmin(this);
            ajouterVue("admin", adminView);
        }
    }
}
