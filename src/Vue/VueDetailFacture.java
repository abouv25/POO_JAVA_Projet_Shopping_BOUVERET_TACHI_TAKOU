package Vue;

import DAO.FactureDAO;
import modele.LignePanier;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VueDetailFacture extends JFrame {

    private JTable tableLignes;
    private JLabel labelTotal;
    private JButton boutonExporter;
    private Utilisateur utilisateur;

    public VueDetailFacture(int idFacture, Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Détails de la facture #" + idFacture);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableLignes = new JTable();
        labelTotal = new JLabel("Total : ");
        boutonExporter = new JButton("Exporter en PDF");

        JPanel bas = new JPanel(new BorderLayout());
        bas.add(labelTotal, BorderLayout.WEST);
        bas.add(boutonExporter, BorderLayout.EAST);

        add(new JScrollPane(tableLignes), BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        chargerLignes(idFacture);

        boutonExporter.addActionListener(e -> exporterPDF(idFacture));
    }

    private void chargerLignes(int idFacture) {
        FactureDAO dao = new FactureDAO();
        List<LignePanier> lignes = dao.listerLignesParFacture(idFacture);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix", "Quantité", "Sous-total"}, 0);

        double total = 0.0;
        for (LignePanier l : lignes) {
            double sousTotal = l.getPrix() * l.getQuantite();
            model.addRow(new Object[] {
                    l.getNomProduit(),
                    String.format("%.2f", l.getPrix()) + " €",
                    l.getQuantite(),
                    String.format("%.2f", sousTotal) + " €"
            });
            total += sousTotal;
        }

        tableLignes.setModel(model);
        labelTotal.setText("Total : " + String.format("%.2f", total) + " €");
    }

    private void exporterPDF(int idFacture) {
        // Implémentation de l'exportation PDF (identique à celle que vous avez fournie)
    }
}
