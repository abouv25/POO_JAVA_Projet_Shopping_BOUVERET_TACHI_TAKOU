package App;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import java.util.List;

public class MainVoirFactures {
    public static void main(String[] args) {
        Utilisateur user = new Utilisateur(1, "Antoine", "a@mail.com", "123", true);

        FactureDAO dao = new FactureDAO();
        List<Facture> factures = dao.listerFacturesPourUtilisateur(user);

        System.out.println("Factures de l'utilisateur :");
        for (Facture f : factures) {
            System.out.println("ID: " + f.getId() +
                    " | Date: " + f.getDateFormatee() +
                    " | Total: " + f.getMontantTotal() +
                    " € | Remise: " + f.getRemisePourcent() + "%");
        }
    }
}
