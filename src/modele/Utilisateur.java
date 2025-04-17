package modele;

/**
 * Classe représentant un utilisateur/client de l'application.
 */
public class Utilisateur {
    private int id;
    private String nom;
    private String prenom;         // ✅ Nouveau champ
    private String email;
    private String motDePasse;
    private boolean estAdmin;      // ✅ Nouveau champ
    private boolean clientFidele;

    // Constructeurs
    public Utilisateur() {}

    // Constructeur sans admin/prenom (backward compatibility)
    public Utilisateur(int id, String nom, String email, String motDePasse, boolean clientFidele) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.clientFidele = clientFidele;
    }

    // ✅ Constructeur complet
    public Utilisateur(int id, String nom, String prenom, String email, String motDePasse, boolean estAdmin, boolean clientFidele) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.estAdmin = estAdmin;
        this.clientFidele = clientFidele;
    }

    // Getters / Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public boolean isEstAdmin() { return estAdmin; }
    public void setEstAdmin(boolean estAdmin) { this.estAdmin = estAdmin; }

    public boolean isClientFidele() { return clientFidele; }
    public void setClientFidele(boolean clientFidele) { this.clientFidele = clientFidele; }

    @Override
    public String toString() {
        return prenom + " " + nom + " <" + email + ">" + (estAdmin ? " [ADMIN]" : "");
    }
}
