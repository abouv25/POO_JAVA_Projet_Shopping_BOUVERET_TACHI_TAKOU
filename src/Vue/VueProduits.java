package Vue;

import DAO.ProduitDAO;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class VueProduits extends JPanel {

    private MainWindow mainWindow;
    private Utilisateur utilisateur;
    private JPanel panelCartes;
    private JScrollPane scrollPane;

    public VueProduits(MainWindow mainWindow, Utilisateur utilisateur) {
        this.mainWindow = mainWindow;
        this.utilisateur = utilisateur;

        setLayout(new BorderLayout());

        // ✅ Barre supérieure (logo, utilisateur, panier)
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Panel central pour les cartes produit
        panelCartes = new JPanel(new GridLayout(0, 3, 20, 20));
        panelCartes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCartes.setBackground(new Color(250, 250, 250));

        scrollPane = new JScrollPane(panelCartes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // défilement plus fluide
        add(scrollPane, BorderLayout.CENTER);

        chargerProduits();
    }

    private void chargerProduits() {
        panelCartes.removeAll();
        ProduitDAO dao = new ProduitDAO();
        List<Produit> produits = dao.listerProduits();

        for (Produit produit : produits) {
            panelCartes.add(creerCarteProduit(produit));
        }

        panelCartes.revalidate();
        panelCartes.repaint();
    }

    private JPanel creerCarteProduit(Produit produit) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(200, 260));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // 🌄 Image
        JLabel imageLabel;
        File imageFile = new File(produit.getImage());
        if (imageFile.exists()) {
            ImageIcon icon = new ImageIcon(produit.getImage());
            Image img = icon.getImage().getScaledInstance(160, 100, Image.SCALE_SMOOTH);
            imageLabel = new JLabel(new ImageIcon(img));
        } else {
            imageLabel = new JLabel("[Aucune image]");
        }
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(imageLabel);

        carte.add(Box.createVerticalStrut(10));

        // 🏷️ Nom
        JLabel nomLabel = new JLabel(produit.getNom(), SwingConstants.CENTER);
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(nomLabel);

        // 💶 Prix
        JLabel prixLabel = new JLabel(String.format("%.2f €", produit.getPrix()), SwingConstants.CENTER);
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(prixLabel);

        // 📦 Stock
        JLabel stockLabel = new JLabel("Stock : " + produit.getQuantiteStock(), SwingConstants.CENTER);
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(stockLabel);

        carte.add(Box.createVerticalStrut(10));

        // ➕ Bouton
        JButton boutonAjouter = new JButton("Ajouter au panier");
        boutonAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonAjouter.addActionListener((ActionEvent e) -> {
            String input = JOptionPane.showInputDialog(this, "Quantité à ajouter :", "1");
            if (input == null) return;
            try {
                int qte = Integer.parseInt(input);
                if (qte <= 0) throw new NumberFormatException();
                mainWindow.getPanier().ajouterProduit(produit.getId(), produit.getNom(), produit.getPrix(), qte);
                JOptionPane.showMessageDialog(this, qte + " x " + produit.getNom() + " ajouté(s) au panier.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantité invalide.");
            }
        });

        carte.add(boutonAjouter);
        return carte;
    }
}
