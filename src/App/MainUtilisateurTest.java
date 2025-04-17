/*package App;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import java.util.List;

public class MainUtilisateurTest {
    public static void main(String[] args) {

        UtilisateurDAO dao = new UtilisateurDAO();

        // 1. ➕ Test ajout utilisateur
        Utilisateur nouvelUtilisateur = new Utilisateur(0, "Jean Dupont", "jean@example.com", "mdp123", true);
        boolean ajoute = dao.ajouterUtilisateur(nouvelUtilisateur);
        System.out.println("Ajout réussi ? " + ajoute);

        // 2. 🔍 Test login
        Utilisateur trouve = dao.trouverParEmailEtMotDePasse("jean@example.com", "mdp123");
        System.out.println("Utilisateur trouvé : " + (trouve != null ? trouve.getNom() : "Aucun"));

        // 3. ✏️ Test modification
        if (trouve != null) {
            trouve.setNom("Jean Modifié");
            trouve.setClientFidele(false);
            boolean modifie = dao.modifierUtilisateur(trouve);
            System.out.println("Modification réussie ? " + modifie);
        }

        // 4. 📋 Test liste
        List<Utilisateur> liste = dao.listerUtilisateurs();
        System.out.println("Liste des utilisateurs :");
        for (Utilisateur u : liste) {
            System.out.println(" - " + u.getId() + " | " + u.getNom() + " | " + u.getEmail());
        }

        // 5. 🗑️ Test suppression
        if (trouve != null) {
            boolean supprime = dao.supprimerUtilisateur(trouve.getId());
            System.out.println("Suppression réussie ? " + supprime);
        }
    }
}*/
