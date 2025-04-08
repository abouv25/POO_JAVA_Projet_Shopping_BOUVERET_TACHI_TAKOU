package App;//projet debute le 02.04.25 par Antoine Bouveret, Bryan Tachi et Boris Takou.


import DAO.ProduitDAO;
import modele.Produit;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProduitDAO produitDAO = new ProduitDAO();

        // Test : ajouter un produit
        Produit nouveauProduit = new Produit(0, "Casque Bluetooth", 49.99, 10);
        boolean ajoute = produitDAO.ajouterProduit(nouveauProduit);
        System.out.println("Produit ajouté : " + ajoute);

        // Test : récupérer tous les produits
        List<Produit> produits = produitDAO.getTousLesProduits();
        System.out.println("Liste des produits :");
        for (Produit p : produits) {
            System.out.println(p);
        }
    }
}
