package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.LignePanier;
import modele.Utilisateur;
import modele.Produit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VueFacture extends JFrame {

    private JTable tableFacture;
    private JLabel labelTotal;
    private JLabel labelRemise;
    private JButton boutonConfirmer;

    public VueFacture(Utilisateur utilisateur, List<LignePanier> lignes) {
        setTitle("Facture");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        tableFacture = new JTable();
        chargerLignes(lignes);
        add(new JScrollPane(tableFacture), BorderLayout.CENTER);

        // Bas de fenêtre : Total + Remise + Bouton
        JPanel bas = new JPanel(new GridLayout(3, 1));
        labelRemise = new JLabel();
        labelTotal = new JLabel();
        boutonConfirmer = new JButton("Confirmer la commande");

        bas.add(labelRemise);
        bas.add(labelTotal);
        bas.add(boutonConfirmer);

        add(bas, BorderLayout.SOUTH);

        // Calcul de la remise / total
        double totalBrut = lignes.stream().mapToDouble(LignePanier::getSousTotal).sum();
        double remise = utilisateur.isClientFidele() ? 0.10 * totalBrut : 0.0;
        double totalFinal = totalBrut - remise;

        labelRemise.setText("Remise : " + (utilisateur.isClientFidele() ? "10%" : "Aucune"));
        labelTotal.setText("Total à payer : " + String.format("%.2f", totalFinal) + " €");

        // Action : enregistrement de la facture
        boutonConfirmer.addActionListener(e -> {
            Facture facture = new Facture(utilisateur, lignes);
            FactureDAO dao = new FactureDAO();
            boolean ok = dao.ajouterFacture(facture);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Facture enregistrée avec succès !");
                dispose(); // Ferme la fenêtre
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.");
            }
        });
    }

    private void chargerLignes(List<LignePanier> lignes) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix unitaire", "Quantité", "Sous-total"}, 0);

        for (LignePanier l : lignes) {
            model.addRow(new Object[] {
                    l.getNomProduit(),
                    String.format("%.2f", l.getPrix()) + " €",
                    l.getQuantite(),
                    String.format("%.2f", l.getSousTotal()) + " €"  // Utilisation de getSousTotal
            });
        }

        tableFacture.setModel(model);
    }

    public static void main(String[] args) {
        // Exemple de test : créer un utilisateur factice et quelques lignes
        Utilisateur user = new Utilisateur(1, "Antoine", "a@mail.com", "1234", true);

        List<LignePanier> lignes = List.of(
                new LignePanier(1, new Produit(1, "T-shirt", 15.0,2), 2),
                new LignePanier(2, new Produit(2, "Casquette", 12.5,2), 1)
        );

        SwingUtilities.invokeLater(() -> new VueFacture(user, lignes).setVisible(true));
    }
}
