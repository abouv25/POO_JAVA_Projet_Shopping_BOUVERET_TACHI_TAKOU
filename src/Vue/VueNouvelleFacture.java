package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.LignePanier;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VueNouvelleFacture extends JPanel {

    private JTable tableFacture;
    private JLabel labelTotal;
    private JLabel labelRemise;
    private JButton boutonConfirmer;
    private JButton boutonAccueil;
    private MainWindow mainWindow;

    public VueNouvelleFacture(MainWindow mainWindow, Utilisateur utilisateur, List<LignePanier> lignes) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());

        // Table
        tableFacture = new JTable();
        chargerLignes(lignes);
        add(new JScrollPane(tableFacture), BorderLayout.CENTER);

        // Bas de fenêtre : Total + Remise + Boutons
        JPanel bas = new JPanel(new BorderLayout());

        JPanel infos = new JPanel(new GridLayout(2, 1));
        labelRemise = new JLabel();
        labelTotal = new JLabel();
        infos.add(labelRemise);
        infos.add(labelTotal);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutonConfirmer = new JButton("Confirmer la commande");
        boutonAccueil = new JButton("Accueil");
        boutons.add(boutonAccueil);
        boutons.add(boutonConfirmer);

        bas.add(infos, BorderLayout.WEST);
        bas.add(boutons, BorderLayout.EAST);

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
                JOptionPane.showMessageDialog(mainWindow, "Facture enregistrée avec succès !");
                mainWindow.switchTo("accueil");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Erreur lors de l'enregistrement.");
            }
        });

        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
    }

    private void chargerLignes(List<LignePanier> lignes) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix unitaire", "Quantité", "Sous-total"}, 0);

        for (LignePanier l : lignes) {
            model.addRow(new Object[]{
                    l.getNomProduit(),
                    String.format("%.2f", l.getPrix()) + " €",
                    l.getQuantite(),
                    String.format("%.2f", l.getSousTotal()) + " €"
            });
        }

        tableFacture.setModel(model);
    }
}
