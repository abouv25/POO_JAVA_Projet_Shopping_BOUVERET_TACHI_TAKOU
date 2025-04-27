package modele;

public class Reduction {
    private int id;
    private double pourcentage;

    // Constructeur sans ID (pour création)
    public Reduction(double pourcentage) {
        this.pourcentage = pourcentage;
    }

    // Constructeur complet
    public Reduction(int id, double pourcentage) {
        this.id = id;
        this.pourcentage = pourcentage;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(double pourcentage) {
        this.pourcentage = pourcentage;
    }
}
