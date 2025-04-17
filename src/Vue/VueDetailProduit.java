package Vue;

import DAO.ProduitDAO;
import modele.Produit;

import javax.swing.*;
import java.awt.*;
import java.io.File;

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
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // 🖼️ Panel image
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (produit.getImage() != null && !produit.getImage().isBlank()) {
            File imageFile = new File(produit.getImage());
            if (imageFile.exists()) {
                ImageIcon icon = new ImageIcon(produit.getImage());
                Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(img));
                imagePanel.add(imageLabel);
            } else {
                imagePanel.add(new JLabel("Image introuvable"));
            }
        } else {
            imagePanel.add(new JLabel("Aucune image"));
        }

        add(imagePanel, BorderLayout.NORTH);

        // 📝 Formulaire
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        champNom = new JTextField(produit.getNom());
        champPrix = new JTextField(String.valueOf(produit.getPrix()));
        champStock = new JTextField(String.valueOf(produit.getQuantiteStock()));

        form.add(new JLabel("Nom :"));
        form.add(champNom);
        form.add(new JLabel("Prix (€) :"));
        form.add(champPrix);
        form.add(new JLabel("Stock :"));
        form.add(champStock);

        add(form, BorderLayout.CENTER);

        // 🔘 Boutons
        JPanel bas = new JPanel(new FlowLayout());
        boutonEnregistrer = new JButton("💾 Enregistrer");
        boutonAccueil = new JButton("🏠 Accueil");

        bas.add(boutonEnregistrer);
        bas.add(boutonAccueil);

        add(bas, BorderLayout.SOUTH);

        // Actions
        boutonEnregistrer.addActionListener(e -> enregistrerModifications());
        boutonAccueil.addActionListener(e -> dispose());
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
