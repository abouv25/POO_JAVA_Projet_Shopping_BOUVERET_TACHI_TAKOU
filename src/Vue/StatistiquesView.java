package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class StatistiquesView extends JPanel {

    public StatistiquesView(MainWindow mainWindow) {
        setLayout(new BorderLayout());

        // Récupérer les statistiques des ventes
        FactureDAO factureDAO = new FactureDAO();
        Utilisateur utilisateur = new Utilisateur(1, "John Doe", "john@example.com", "password", true);
        List<Facture> factures = factureDAO.listerFacturesPourUtilisateur(utilisateur);

        // Calculer les statistiques par produit
        Map<Produit, Integer> ventesParProduit = factureDAO.getVentesParProduit(factures);

        // Créer le modèle de table pour afficher les statistiques
        String[] columns = {"Produit", "Quantité Vendue"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Map.Entry<Produit, Integer> entry : ventesParProduit.entrySet()) {
            tableModel.addRow(new Object[]{entry.getKey().getNom(), entry.getValue()});
        }

        // Créer la JTable pour afficher les données
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Ajouter un bouton pour revenir à l'accueil
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> mainWindow.switchTo("accueil"));
        add(backButton, BorderLayout.SOUTH);
    }
}
