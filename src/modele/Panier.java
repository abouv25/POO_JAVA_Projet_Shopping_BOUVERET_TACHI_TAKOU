package modele;

import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant le panier d'un utilisateur.
 */
public class Panier {
    private Map<Produit, Integer> produits;

    public Panier() {
        produits = new HashMap<>();
    }

    // Ajouter un produit au panier
    public void ajouterProduit(Produit produit, int quantite) {
        produits.put(produit, produits.getOrDefault(produit, 0) + quantite);
    }

    // Supprimer un produit
    public void supprimerProduit(Produit produit) {
        produits.remove(produit);
    }

    // Modifier la quantité d’un produit
    public void modifierQuantite(Produit produit, int nouvelleQuantite) {
        if (produits.containsKey(produit)) {
            if (nouvelleQuantite <= 0) {
                produits.remove(produit);
            } else {
                produits.put(produit, nouvelleQuantite);
            }
        }
    }

    // Calculer le total
    public double calculerTotal() {
        double total = 0.0;
        for (Map.Entry<Produit, Integer> entry : produits.entrySet()) {
            total += entry.getKey().getPrix() * entry.getValue();
        }
        return total;
    }

    // Getter
    public Map<Produit, Integer> getProduits() {
        return produits;
    }
}
