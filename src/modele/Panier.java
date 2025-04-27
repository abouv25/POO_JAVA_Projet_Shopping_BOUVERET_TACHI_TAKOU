package modele;

import java.util.ArrayList;
import java.util.List;

public class Panier {

    private List<LignePanier> lignes;

    public Panier() {
        this.lignes = new ArrayList<>();
    }

    // Ajouter un produit au panier
    public void ajouterProduit(int idProduit, String nomProduit, double prix, int quantite) {
        for (LignePanier l : lignes) {
            if (l.getIdProduit() == idProduit) {
                l.setQuantite(l.getQuantite() + quantite);  // Si le produit existe déjà, on augmente la quantité
                return;
            }
        }
        lignes.add(new LignePanier(idProduit, nomProduit, prix, quantite)); // Sinon, on ajoute un nouveau produit
    }

    // Modifier la quantité d'un produit
    public void modifierQuantite(int idProduit, int nouvelleQuantite) {
        for (LignePanier l : lignes) {
            if (l.getIdProduit() == idProduit) {
                if (nouvelleQuantite == 0) {
                    retirerProduit(idProduit);  // Si la quantité est 0, on retire le produit
                } else {
                    l.setQuantite(nouvelleQuantite);  // Sinon, on met à jour la quantité
                }
                return;
            }
        }
    }

    // Retirer un produit du panier
    public void retirerProduit(int idProduit) {
        lignes.removeIf(l -> l.getIdProduit() == idProduit);
    }

    // Vider le panier
    public void viderPanier() {
        lignes.clear();
    }

    // Calculer le total brut du panier
    public double calculerTotalBrut() {
        return lignes.stream().mapToDouble(LignePanier::getSousTotal).sum();
    }

    // Récupérer toutes les lignes du panier
    public List<LignePanier> getLignes() {
        return lignes;
    }

    // Vérifier si le panier est vide
    public boolean estVide() {
        return lignes.isEmpty();
    }

}
