package modele;

public class LignePanier {
    private int idProduit;
    private Produit produit;
    private int quantite;

    // Constructeur
    public LignePanier(int idProduit, Produit produit, int quantite) {
        this.idProduit = idProduit;
        this.produit = produit;
        this.quantite = quantite;
    }

    // Méthode pour obtenir le nom du produit
    public String getNomProduit() {
        return this.produit.getNom();  // Accède au nom du produit
    }

    // Méthode pour obtenir le prix du produit
    public double getPrix() {
        return this.produit.getPrix();  // Accède au prix du produit
    }

    // Ajout de la méthode getSousTotal pour calculer le sous-total
    public double getSousTotal() {
        return this.produit.getPrix() * this.quantite;
    }

    // Getters et setters
    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
}
