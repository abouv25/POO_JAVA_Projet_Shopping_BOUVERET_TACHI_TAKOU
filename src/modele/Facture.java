package modele;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe représentant une facture générée à partir d’un panier.
 */
public class Facture {
    private Utilisateur client;
    private Panier panier;
    private LocalDate date;
    private double remisePourcent; // par exemple 10% pour client fidèle

    public Facture(Utilisateur client, Panier panier) {
        this.client = client;
        this.panier = panier;
        this.date = LocalDate.now();
        this.remisePourcent = client.isClientFidele() ? 10.0 : 0.0;
    }

    // Calcul du total avec remise
    public double calculerTotal() {
        double totalBrut = panier.calculerTotal();
        double remise = totalBrut * (remisePourcent / 100.0);
        return totalBrut - remise;
    }

    // Getter de la date formatée
    public String getDateFormatee() {
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public Utilisateur getClient() {
        return client;
    }

    public Panier getPanier() {
        return panier;
    }

    public double getRemisePourcent() {
        return remisePourcent;
    }

    public LocalDate getDate() {
        return date;
    }
}
