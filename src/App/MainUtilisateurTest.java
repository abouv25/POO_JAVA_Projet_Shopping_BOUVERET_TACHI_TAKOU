package App;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

public class MainUtilisateurTest {
    public static void main(String[] args) {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

        // Ajouter un utilisateur
        Utilisateur u = new Utilisateur(0, "Antoine Bouveret", "antoine@test.com", "password123", true);
        boolean ajoute = utilisateurDAO.ajouterUtilisateur(u);
        System.out.println("Utilisateur ajouté : " + ajoute);

        // Rechercher l'utilisateur
        Utilisateur retrouve = utilisateurDAO.trouverParEmailEtMotDePasse("antoine@test.com", "password123");

        if (retrouve != null) {
            System.out.println("Connexion réussie : " + retrouve.getNom());
        } else {
            System.out.println("Utilisateur non trouvé ou mot de passe incorrect.");
        }
    }
}
