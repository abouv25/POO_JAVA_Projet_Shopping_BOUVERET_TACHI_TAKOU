package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import modele.Produit;  // Assurez-vous d'importer la classe Produit

public class VuePanier extends JPanel {

    private MainWindow mainWindow;
    private JTable table;
    private DefaultTableModel tableModel;
    private ArrayList<Produit> panier; // Liste des produits dans le panier
    private double totalPanier;

    public VuePanier(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.panier = new ArrayList<>();
        this.totalPanier = 0.0;

        setLayout(new BorderLayout());

        // Table pour afficher le contenu du panier
        String[] columns = {"Nom du produit", "Quantité", "Prix Unitaire", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panneau pour les boutons et le total du panier
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // Total du panier
        JLabel totalLabel = new JLabel("Total du panier: " + totalPanier + " €");
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        // Boutons pour ajouter et supprimer des produits
        JButton addButton = new JButton("Ajouter un produit");
        JButton removeButton = new JButton("Supprimer le produit sélectionné");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Exemple d'ajout d'un produit (vous pouvez lier cela à un formulaire d'ajout de produit)
                Produit newProduct = new Produit(1, "Produit Exemple", 20.0, 5); // Exemple de produit ajouté
                addProductToPanier(newProduct);
                totalLabel.setText("Total du panier: " + totalPanier + " €");
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    removeProductFromPanier(selectedRow);
                    totalLabel.setText("Total du panier: " + totalPanier + " €");
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Veuillez sélectionner un produit à supprimer.");
                }
            }
        });

        bottomPanel.add(addButton, BorderLayout.WEST);
        bottomPanel.add(removeButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Méthode pour ajouter un produit au panier
    private void addProductToPanier(Produit produit) {
        panier.add(produit);
        tableModel.addRow(new Object[]{
                produit.getNom(),
                1, // Quantité de produit (vous pouvez ajouter un champ pour la modifier)
                produit.getPrix(),
                produit.getPrix()  // Total du produit (quantité * prix)
        });
        totalPanier += produit.getPrix();  // Mise à jour du total du panier
    }

    // Méthode pour supprimer un produit du panier
    private void removeProductFromPanier(int rowIndex) {
        Produit productToRemove = panier.get(rowIndex);
        panier.remove(rowIndex);
        tableModel.removeRow(rowIndex);
        totalPanier -= productToRemove.getPrix();  // Mise à jour du total du panier
    }
}
