package App;

import DAO.ProduitDAO;
import modele.Produit;

import java.util.List;

public class InitialisationProduits {

    public static void main(String[] args) {
        ProduitDAO dao = new ProduitDAO();

        // Vérifie si la base contient déjà des produits
        List<Produit> existants = dao.listerProduits();
        if (!existants.isEmpty()) {
            System.out.println("Des produits existent déjà. Aucun ajout nécessaire.");
            return;
        }

        Produit[] produits = {
                new Produit(0, "T-shirt Coton Bio", 19.99, 50, "images/tshirt.png"),
                new Produit(0, "Sweat Hoodie Bleu", 34.90, 30, "images/sweat.png"),
                new Produit(0, "Casquette Stylée", 14.50, 70, "images/casquette.png"),
                new Produit(0, "Jean Slim Noir", 44.95, 25, "images/jean.png"),
                new Produit(0, "Sac à dos urbain", 59.00, 20, "images/sac.png"),
                new Produit(0, "Montre Digitale", 89.99, 15, "images/montre.png"),
                new Produit(0, "Lunettes de soleil", 22.30, 40, "images/lunettes.png"),
                new Produit(0, "Chaussures Running", 69.00, 35, "images/chaussures.png"),
                new Produit(0, "Ceinture Cuir", 18.99, 60, "images/ceinture.png"),
                new Produit(0, "Gourde Inox", 12.00, 80, "images/gourde.png")
        };

        for (Produit p : produits) {
            boolean ok = dao.ajouterProduit(p);
            System.out.println((ok ? "✔️ Ajouté : " : "❌ Échec : ") + p.getNom());
        }

        System.out.println("Initialisation terminée.");
    }
}
