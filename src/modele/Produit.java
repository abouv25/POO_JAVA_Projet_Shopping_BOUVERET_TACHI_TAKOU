package modele;

/**
 * Classe représentant un produit disponible à la vente.
 */
public class Produit {
    private int id;
    private String nom;
    private double prix;
    private int quantiteStock;
    private String image; // 🆕 champ image

    // Constructeurs
    public Produit() {}

    public Produit(int id, String nom, double prix, int quantiteStock) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
    }

    // 🆕 Nouveau constructeur avec image
    public Produit(int id, String nom, double prix, int quantiteStock, String image) {
        this.id = id;
        this.nom = nom;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.image = image;
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

    public int getStock() {
        return getQuantiteStock();
    }

    public String getImage() {  // ✅ Getter pour image
        return image;
    }

    public void setImage(String image) { // ✅ Setter pour image
        this.image = image;
    }

    @Override
    public String toString() {
        return nom + " (" + prix + " €)";
    }
}
