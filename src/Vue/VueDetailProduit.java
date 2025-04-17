package Vue;

import DAO.ProduitDAO;
import modele.Produit;

import javax.swing.*;
import java.awt.*;

public class VueDetailProduit extends JFrame {

    private JTextField champNom;
    private JTextField champPrix;
    private JTextField champStock;
    private JButton boutonEnregistrer;
    private JButton boutonAccueil;
    private Produit produit;

    public VueDetailProduit(Produit produit) {
        this.produit = produit;

        setTitle("📝 Détail du produit : " + produit.getNom());
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Formulaire
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        champNom = new JTextField(produit.getNom());
        champPrix = new JTextField(String.valueOf(produit.getPrix()));
        champStock = new JTextField(String.valueOf(produit.getQuantiteStock()));

        form.add(new JLabel("Nom :"));
        form.add(champNom);
        form.add(new JLabel("Prix (€) :"));
        form.add(champPrix);
        form.add(new JLabel("Stock :"));
        form.add(champStock);

        // Bas
        JPanel bas = new JPanel(new FlowLayout());
        boutonEnregistrer = new JButton("💾 Enregistrer");
        boutonAccueil = new JButton("🏠 Accueil");

        bas.add(boutonEnregistrer);
        bas.add(boutonAccueil);

        add(form, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        // Actions
        boutonEnregistrer.addActionListener(e -> enregistrerModifications());
        boutonAccueil.addActionListener(e -> dispose()); // ou switchTo("accueil") si intégré
    }

    private void enregistrerModifications() {
        String nom = champNom.getText().trim();
        String prixStr = champPrix.getText().trim();
        String stockStr = champStock.getText().trim();

        try {
            double prix = Double.parseDouble(prixStr);
            int stock = Integer.parseInt(stockStr);

            produit.setNom(nom);
            produit.setPrix(prix);
            produit.setQuantiteStock(stock);

            ProduitDAO dao = new ProduitDAO();
            if (dao.modifierProduit(produit)) {
                JOptionPane.showMessageDialog(this, "Produit modifié avec succès !");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrée invalide.");
        }
    }
}
