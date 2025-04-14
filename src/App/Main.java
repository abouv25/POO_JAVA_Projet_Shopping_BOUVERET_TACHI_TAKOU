package App;

import Vue.VueHistoriqueFactures;
import modele.Facture;
import modele.Utilisateur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Créez un utilisateur exemple (vous pouvez adapter les détails selon votre projet)
        Utilisateur utilisateur = new Utilisateur(1, "Jean Dupont", "jean.dupont@example.com", "motdepasse", true);

        // Simuler des factures pour cet utilisateur (vous pouvez adapter la logique pour récupérer les vraies factures)
        List<Facture> factures = getFacturesUtilisateur(utilisateur);

        // Créer une table pour afficher les factures
        JTable tableFactures = new JTable(new DefaultTableModel(new Object[]{"ID", "Date", "Montant"}, 0));
        JButton boutonVoirFacture = new JButton("Voir Détails");
        JLabel labelUtilisateur = new JLabel("Utilisateur : " + utilisateur.getNom());

        // Lancer la fenêtre de l'historique des factures pour l'utilisateur
        new VueHistoriqueFactures(utilisateur).setVisible(true);
    }

    // Méthode simulée pour récupérer les factures d'un utilisateur
    private static List<Facture> getFacturesUtilisateur(Utilisateur utilisateur) {
        // Exemple simulé, remplacez cela par l'accès réel aux données
        return List.of(
                new Facture(1, utilisateur, LocalDate.now(), 100.0),  // Facture simulée
                new Facture(2, utilisateur, LocalDate.now().minusDays(5), 200.0)  // Autre facture simulée
        );
    }
}
