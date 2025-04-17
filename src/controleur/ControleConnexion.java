package controleur;

import DAO.UtilisateurDAO;
import modele.Utilisateur;
import Vue.VueLogin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Contrôleur pour gérer la connexion utilisateur depuis la VueLogin.
 */
public class ControleConnexion implements ActionListener {

    private VueLogin vue;
    private UtilisateurDAO utilisateurDAO;

    public ControleConnexion(VueLogin vue) {
        this.vue = vue;
        this.utilisateurDAO = new UtilisateurDAO();

        // On branche le bouton à ce contrôleur
        this.vue.ajouterListenerConnexion(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email = vue.getEmail();
        String motDePasse = vue.getMotDePasse();

        Utilisateur u = utilisateurDAO.verifierConnexion(email, motDePasse);


        if (u != null) {
            vue.afficherMessage("✅ Connexion réussie. Bienvenue " + u.getNom() + " !");
            // ➕ Tu pourras ici ensuite rediriger vers VueCatalogue
        } else {
            vue.afficherMessage("❌ Email ou mot de passe incorrect.");
        }
    }
}
