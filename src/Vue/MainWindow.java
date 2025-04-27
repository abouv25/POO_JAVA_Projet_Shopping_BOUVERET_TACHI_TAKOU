package Vue;

import modele.Panier;
import modele.Utilisateur;
import modele.Facture;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Utilisateur utilisateurConnecte;
    private Panier panier = new Panier();
    private Map<String, JPanel> vues = new HashMap<>();
    private Stack<String> historiqueVues = new Stack<>();
    private String vueActuelle;

    public MainWindow() {
        setTitle("Application BTT Shopping");
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
        ajouterVue("accueilAdmin", new VueAccueilAdmin(this));
        ajouterVue("historique", new VueHistoriqueCommandes(this));
        ajouterVue("statistiques", new VueStatistiques(this));
        ajouterVue("motdepasseoublie", new VueMotDePasseOublie(this));
        ajouterVue("moncompte", new VueMonCompte(this));
        ajouterVue("paiement", new VuePaiement(this));

        // Préchargement du catalogue
        chargerCatalogue();

        // Affichage de la première vue
        setContentPane(mainPanel);
        switchTo("accueil");

        setVisible(true);
    }

    // --- Fonctions principales ---
    public void ajouterVue(String nom, JPanel vue) {
        mainPanel.add(vue, nom);
        vues.put(nom, vue);
    }

    public void supprimerVue(String nom) {
        Component comp = vues.get(nom);
        if (comp != null) {
            mainPanel.remove(comp);
            vues.remove(nom);
        }
    }

    public void switchTo(String viewName) {
        if (!vues.containsKey(viewName)) {
            System.err.println("Vue inconnue : " + viewName);
            return;
        }

        if (vueActuelle != null && !vueActuelle.equals(viewName)) {
            historiqueVues.push(vueActuelle);
        }
        vueActuelle = viewName;
        cardLayout.show(mainPanel, viewName);

        if ("accueil".equals(viewName)) {
            VueAccueil accueil = (VueAccueil) vues.get("accueil");
            accueil.mettreAJourAffichage();
        }
        if ("panier".equals(viewName)) {
            VuePanier panierView = (VuePanier) vues.get("panier");
            panierView.rafraichir();
        }
        if ("catalogue".equals(viewName)) {
            chargerCatalogue();
        }
    }

    public void retourPagePrecedente() {
        if (!historiqueVues.isEmpty()) {
            String precedente = historiqueVues.pop();
            vueActuelle = precedente;
            cardLayout.show(mainPanel, precedente);
        }
    }

    // --- Accesseurs ---
    public Panier getPanier() {
        return panier;
    }

    public Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public void setUtilisateurConnecte(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
    }

    public Map<String, JPanel> getVues() {
        return vues;
    }

    // --- Chargement dynamique sécurisé ---
    public void chargerCatalogue() {
        if (!vues.containsKey("catalogue")) {
            VueProduits vueProduits = new VueProduits(this, utilisateurConnecte);
            ajouterVue("catalogue", vueProduits);
        }
    }

    public void chargerVueAdmin() {
        if (!vues.containsKey("admin")) {
            VueAdmin adminView = new VueAdmin(this);
            ajouterVue("admin", adminView);
        }
    }

    public void chargerVueGestionUtilisateurs() {
        if (!vues.containsKey("gestionUtilisateurs")) {
            VueGestionUtilisateurs vue = new VueGestionUtilisateurs(this);
            ajouterVue("gestionUtilisateurs", vue);
        }
    }

    public void chargerVueReductions() {
        if (!vues.containsKey("reductions")) {
            VueGestionReductions vue = new VueGestionReductions(this);
            ajouterVue("reductions", vue);
        }
    }

    public void chargerVueHistoriqueCommandesAdmin() {
        if (!vues.containsKey("historiqueAdmin")) {
            VueHistoriqueCommandesAdmin vue = new VueHistoriqueCommandesAdmin(this);
            ajouterVue("historiqueAdmin", vue);
        }
    }

    public void chargerVueMonCompte() {
        if (!vues.containsKey("moncompte")) {
            VueMonCompte vue = new VueMonCompte(this);
            ajouterVue("moncompte", vue);
        }
    }

    public void chargerVueDetailFacture(int idFacture, Utilisateur client) {
        VueDetailFacture vue = new VueDetailFacture(idFacture, client, this);
        ajouterVue("detailFacture" + idFacture, vue);
        switchTo("detailFacture" + idFacture);
    }

    public void chargerVueNouvelleFacture(Facture facture) {
        VueNouvelleFacture vue = new VueNouvelleFacture(this, facture);
        ajouterVue("nouvelleFacture", vue);
        switchTo("nouvelleFacture");
    }

    public void chargerVueHistoriqueFactures() {
        if (!vues.containsKey("historiqueFactures")) {
            VueHistoriqueFactures vue = new VueHistoriqueFactures(utilisateurConnecte, this);
            ajouterVue("historiqueFactures", vue);
        }
        switchTo("historiqueFactures");
    }
}
