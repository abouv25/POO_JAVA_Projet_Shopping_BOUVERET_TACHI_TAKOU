package modele;

public class Utilisateur {
    private int id;
    private String nom;
    private String email;
    private String motDePasse;
    private boolean clientFidele;

    // Constructeurs
    public Utilisateur(int id, String nom, String email, String motDePasse, boolean clientFidele) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.clientFidele = clientFidele;
    }

    // Getters et setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public boolean isClientFidele() {
        return clientFidele;
    }

    public void setClientFidele(boolean clientFidele) {
        this.clientFidele = clientFidele;
    }
}
