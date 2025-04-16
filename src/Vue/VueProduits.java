package Vue;

import DAO.ProduitDAO;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VueProduits extends JPanel {

    private JTable tableProduits;
    private DefaultTableModel model;
    private JButton boutonAjouterPanier;
    private JButton boutonVoirPanier;
    private JButton boutonAccueil;
    private Utilisateur utilisateur;
    private MainWindow mainWindow;

    public VueProduits(MainWindow mainWindow, Utilisateur utilisateur) {
        this.mainWindow = mainWindow;
        this.utilisateur = utilisateur;

        setLayout(new BorderLayout());

        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Prix (€)", "Stock"}, 0);
        tableProduits = new JTable(model);
        add(new JScrollPane(tableProduits), BorderLayout.CENTER);

        chargerProduits();

        // --- Panneau des boutons ---
        JPanel bas = new JPanel(new FlowLayout());
        boutonAjouterPanier = new JButton("Ajouter au panier");
        boutonVoirPanier = new JButton("Voir le panier");
        boutonAccueil = new JButton("Accueil");

        bas.add(boutonAjouterPanier);
        bas.add(boutonVoirPanier);
        bas.add(boutonAccueil);
        add(bas, BorderLayout.SOUTH);

        boutonAjouterPanier.addActionListener(this::ajouterAuPanier);
        boutonVoirPanier.addActionListener(e -> mainWindow.switchTo("panier"));
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil")); // à adapter
    }

    private void chargerProduits() {
        ProduitDAO dao = new ProduitDAO();
        List<Produit> produits = dao.listerProduits();

        model.setRowCount(0);
        for (Produit p : produits) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getNom(),
                    String.format("%.2f", p.getPrix()),
                    p.getStock()
            });
        }
    }

    private void ajouterAuPanier(ActionEvent e) {
        int row = tableProduits.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit.");
            return;
        }

        int idProduit = (int) model.getValueAt(row, 0);
        String nom = (String) model.getValueAt(row, 1);
        double prix = Double.parseDouble(model.getValueAt(row, 2).toString());

        String input = JOptionPane.showInputDialog(this, "Quantité à ajouter :", "1");
        if (input == null) return;

        try {
            int quantite = Integer.parseInt(input);
            if (quantite <= 0) throw new NumberFormatException();

            // TODO : Ajouter réellement au panier via une classe Panier centralisée
            JOptionPane.showMessageDialog(this, quantite + " x " + nom + " ajouté(s) au panier.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité invalide.");
        }
    }
}
