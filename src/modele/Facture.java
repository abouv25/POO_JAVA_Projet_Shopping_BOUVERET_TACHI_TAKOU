package modele;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe représentant une facture générée à partir d’une liste de lignes.
 */
public class Facture {
    private int id;
    private Utilisateur client;
    private List<LignePanier> lignes;
    private LocalDate date;
    private double remisePourcent;
    private double montantTotal;

    public Facture(Utilisateur client, List<LignePanier> lignes) {
        this.client = client;
        this.lignes = lignes;
        this.date = LocalDate.now();
        this.remisePourcent = client.isClientFidele() ? 10.0 : 0.0;
        this.montantTotal = calculerTotal();
    }

    public Facture(int id, Utilisateur client, LocalDate date, double montantTotal, double remisePourcent) {
        this.id = id;
        this.client = client;
        this.date = date;
        this.montantTotal = montantTotal;
        this.remisePourcent = remisePourcent;
        this.lignes = null; // utilisé uniquement côté affichage
    }
    public Facture(Utilisateur client, List<LignePanier> lignes, double remisePourcent) {
        this.client = client;
        this.lignes = lignes;
        this.date = LocalDate.now();
        this.remisePourcent = remisePourcent;
        this.montantTotal = calculerTotal();
    }


    public double calculerTotal() {
        if (lignes == null) return montantTotal;
        double totalBrut = lignes.stream()
                .mapToDouble(l -> l.getPrix() * l.getQuantite())
                .sum();
        double remise = totalBrut * (remisePourcent / 100.0);
        return totalBrut - remise;
    }

    public void calculerMontantTotal() {
        if (lignes == null) {
            montantTotal = 0;
            return;
        }
        montantTotal = lignes.stream()
                .mapToDouble(ligne -> ligne.getPrix() * ligne.getQuantite())
                .sum();

        // Appliquer la remise s'il y en a
        if (remisePourcent > 0) {
            montantTotal = montantTotal * (1 - remisePourcent / 100.0);
        }
    }


    // Getters / Setters
    public int getId() {
        return id;
    }

    public Utilisateur getClient() {
        return client;
    }

    public List<LignePanier> getLignes() {
        return lignes;
    }

    public double getRemisePourcent() {
        return remisePourcent;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public String getDateFormatee() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
    }
}
