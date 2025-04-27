package modele;

public class Reduction {
    private int id;
    private String nom;
    private double pourcentage;

    // Constructeur vide
    public Reduction() {}

    // 🔥 Nouveau constructeur pour VueCreationReduction
    public Reduction(double pourcentage) {
        this.nom = "";
        this.pourcentage = pourcentage;
    }

    // Constructeur pour création
    public Reduction(String nom, double pourcentage) {
        this.nom = nom;
        this.pourcentage = pourcentage;
    }

    // Constructeur complet
    public Reduction(int id, String nom, double pourcentage) {
        this.id = id;
        this.nom = nom;
        this.pourcentage = pourcentage;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public double getPourcentage() { return pourcentage; }
    public void setPourcentage(double pourcentage) { this.pourcentage = pourcentage; }
}
