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
    private JButton boutonAccueil;
    private MainWindow mainWindow;

    public VueNouvelleFacture(MainWindow mainWindow, Facture facture) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Titre
        JLabel titre = new JLabel("✅ Paiement confirmé - Facture", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titre, BorderLayout.NORTH);

        // ✅ Tableau de la facture
        tableFacture = new JTable();
        chargerLignes(facture.getLignes());
        JScrollPane scrollPane = new JScrollPane(tableFacture);
        add(scrollPane, BorderLayout.CENTER);

        // ✅ Bas : Total + Remise + Accueil
        JPanel bas = new JPanel(new BorderLayout());
        bas.setBackground(getBackground());

        JPanel infos = new JPanel(new GridLayout(2, 1));
        infos.setOpaque(false);
        labelRemise = new JLabel();
        labelTotal = new JLabel();
        infos.add(labelRemise);
        infos.add(labelTotal);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        boutons.setOpaque(false);
        boutonAccueil = new JButton("🏠 Retour à l'accueil");
        StyleUI.styliserBouton(boutonAccueil);
        boutons.add(boutonAccueil);

        bas.add(infos, BorderLayout.WEST);
        bas.add(boutons, BorderLayout.EAST);

        add(bas, BorderLayout.SOUTH);

        // ✅ Calcul du total
        double totalBrut = facture.getLignes().stream().mapToDouble(LignePanier::getSousTotal).sum();
        double remise = facture.getRemisePourcent() / 100.0 * totalBrut;
        double totalFinal = totalBrut - remise;

        labelRemise.setText("Remise appliquée : " + (facture.getRemisePourcent() > 0 ? facture.getRemisePourcent() + "%" : "Aucune"));
        labelTotal.setText("Total payé : " + String.format("%.2f", totalFinal) + " €");

        // ✅ Listeners
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
    }

    private void chargerLignes(List<LignePanier> lignes) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix unitaire", "Quantité", "Sous-total"}, 0
        );

        if (lignes != null) {
            for (LignePanier l : lignes) {
                model.addRow(new Object[]{
                        l.getNomProduit(),
                        String.format("%.2f €", l.getPrix()),
                        l.getQuantite(),
                        String.format("%.2f €", l.getSousTotal())
                });
            }
        }

        tableFacture.setModel(model);
        tableFacture.setRowHeight(28);
    }
}
