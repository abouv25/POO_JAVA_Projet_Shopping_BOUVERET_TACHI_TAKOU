package App;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import java.util.List;

public class MainVoirFactures {
    public static void main(String[] args) {
        // Créer un utilisateur pour tester
        Utilisateur user = new Utilisateur(1, "Antoine", "a@mail.com", "123", true);

        // Créer un objet FactureDAO pour interagir avec la base de données
        FactureDAO dao = new FactureDAO();
        List<Facture> factures = dao.listerFacturesPourUtilisateur(user);  // Récupérer la liste des factures

        // Afficher les informations des factures
        System.out.println("Factures de l'utilisateur :");
        for (Facture f : factures) {
            System.out.println("ID: " + f.getId() +
                    " | Date: " + f.getDateFormatee() +
                    " | Total: " + f.getMontantTotal() +
                    " € | Remise: " + f.getRemisePourcent() + "%");
        }
    }
}
