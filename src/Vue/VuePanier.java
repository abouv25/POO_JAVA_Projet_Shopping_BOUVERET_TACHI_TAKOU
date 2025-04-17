package Vue;

import modele.LignePanier;
import modele.Panier;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VuePanier extends JPanel {

    private MainWindow mainWindow;
    private JTable tablePanier;
    private DefaultTableModel model;
    private JButton boutonValider;
    private JButton boutonVider;
    private JButton boutonRetour;
    private JButton boutonAccueil;
    private JLabel labelTotal;

    public VuePanier(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"Produit", "Prix", "Quantité", "Sous-total"}, 0);
        tablePanier = new JTable(model);
        add(new JScrollPane(tablePanier), BorderLayout.CENTER);

        // Bas : Total + boutons
        JPanel bas = new JPanel(new BorderLayout());

        labelTotal = new JLabel("Total : 0.00 €");
        labelTotal.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel boutons = new JPanel(new FlowLayout());
        boutonAccueil = new JButton("Accueil");
        boutonValider = new JButton("Valider la commande");
        boutonVider = new JButton("Vider le panier");
        boutonRetour = new JButton("Retour catalogue");

        boutons.add(boutonAccueil);
        boutons.add(boutonVider);
        boutons.add(boutonValider);
        boutons.add(boutonRetour);

        bas.add(labelTotal, BorderLayout.NORTH);
        bas.add(boutons, BorderLayout.SOUTH);

        add(bas, BorderLayout.SOUTH);

        // Listeners
        boutonValider.addActionListener(this::validerCommande);
        boutonVider.addActionListener(e -> {
            mainWindow.getPanier().viderPanier();
            rafraichir();
        });
        boutonRetour.addActionListener(e -> mainWindow.switchTo("catalogue"));
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));

        rafraichir();
    }

    public void rafraichir() {
        Panier panier = mainWindow.getPanier();
        List<LignePanier> lignes = panier.getLignes();

        model.setRowCount(0);
        for (LignePanier l : lignes) {
            model.addRow(new Object[]{
                    l.getNomProduit(),
                    String.format("%.2f", l.getPrix()),
                    l.getQuantite(),
                    String.format("%.2f", l.getSousTotal())
            });
        }

        labelTotal.setText("Total : " + String.format("%.2f", panier.calculerTotalBrut()) + " €");
    }

    private void validerCommande(ActionEvent e) {
        Utilisateur utilisateur = mainWindow.getUtilisateurConnecte();
        List<LignePanier> lignes = mainWindow.getPanier().getLignes();

        if (lignes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le panier est vide.");
            return;
        }

        VueNouvelleFacture vueFacture = new VueNouvelleFacture(mainWindow, utilisateur, lignes);
        mainWindow.ajouterVue("facture", vueFacture);
        mainWindow.switchTo("facture");
    }
}
