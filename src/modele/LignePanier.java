package modele;

/**
 * Représente une ligne du panier stockée en base de données
 * et utilisée pour l'affichage dans les interfaces Swing.
 */
public class LignePanier {
    private int idProduit;
    private String nomProduit;
    private double prix;
    private int quantite;

    public LignePanier() {}

    public LignePanier(int idProduit, String nomProduit, double prix, int quantite) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.quantite = quantite;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getSousTotal() {
        return prix * quantite;
    }

    @Override
    public String toString() {
        return nomProduit + " x" + quantite + " (" + prix + "€)";
    }
}
