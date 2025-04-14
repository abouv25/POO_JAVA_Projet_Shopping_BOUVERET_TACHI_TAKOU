package modele;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Facture {
    private int id;
    private Utilisateur client;
    private List<LignePanier> lignes;
    private LocalDate date;
    private double montantTotal;
    private double remisePourcent;  // Remise en pourcentage

    // Constructeur pour récupérer une facture de la base de données
    public Facture(int id, Utilisateur client, LocalDate date, double montantTotal) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.montantTotal = montantTotal;
        this.lignes = new ArrayList<>();
    }

    // Constructeur pour créer une nouvelle facture
    public Facture(Utilisateur client, List<LignePanier> lignes) {
        this.client = client;
        this.lignes = new ArrayList<>(lignes);
        this.date = LocalDate.now();
        this.montantTotal = calculerTotal();
    }

    // Calcul du montant total de la facture
    public double calculerTotal() {
        return lignes.stream()
                .mapToDouble(l -> l.getPrix() * l.getQuantite())
                .sum();
    }

    // Méthode pour obtenir la date formatée
    public String getDateFormatee() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return this.date.format(formatter);
    }

    // Getter et Setter pour la remise en pourcentage
    public double getRemisePourcent() {
        return remisePourcent;
    }

    public void setRemisePourcent(double remisePourcent) {
        this.remisePourcent = remisePourcent;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Utilisateur getClient() {
        return client;
    }

    public void setClient(Utilisateur client) {
        this.client = client;
    }

    public List<LignePanier> getLignes() {
        return new ArrayList<>(lignes);
    }

    public void setLignes(List<LignePanier> lignes) {
        this.lignes = new ArrayList<>(lignes);
        this.montantTotal = calculerTotal();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
}
