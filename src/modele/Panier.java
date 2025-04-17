package modele;

import java.util.ArrayList;
import java.util.List;

public class Panier {

    private List<LignePanier> lignes;

    public Panier() {
        this.lignes = new ArrayList<>();
    }

    public void ajouterProduit(int idProduit, String nomProduit, double prix, int quantite) {
        for (LignePanier l : lignes) {
            if (l.getIdProduit() == idProduit) {
                l.setQuantite(l.getQuantite() + quantite);
                return;
            }
        }
        lignes.add(new LignePanier(idProduit, nomProduit, prix, quantite));
    }

    public void retirerProduit(int idProduit) {
        lignes.removeIf(l -> l.getIdProduit() == idProduit);
    }

    public void viderPanier() {
        lignes.clear();
    }

    public double calculerTotalBrut() {
        return lignes.stream().mapToDouble(LignePanier::getSousTotal).sum();
    }

    public List<LignePanier> getLignes() {
        return lignes;
    }

    public boolean estVide() {
        return lignes.isEmpty();
    }
}
