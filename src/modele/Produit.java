package modele;

public class Produit {
    private int id;
    private String nom;
    private double prix;
    private int quantiteStock;

    // Constructeurs
    public Produit() {}

    public Produit(int id, String nom, double prix, int quantiteStock) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
    }

    // Getters / Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    @Override
    public String toString() {
        return nom + " (" + prix + " €)";
    }
}
