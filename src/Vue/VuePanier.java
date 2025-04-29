package Vue;

import DAO.FactureDAO;
import DAO.ProduitDAO;
import modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VuePanier extends JPanel {

    private final MainWindow mainWindow;
    private final JPanel panelLignes;
    private final JLabel labelResume;
    private final JButton boutonValider;
    private final JButton boutonVider;
    private final JButton boutonRetour;
    private final JButton boutonAccueil;

    public VuePanier(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Zone centrale : liste des produits
        panelLignes = new JPanel();
        panelLignes.setLayout(new BoxLayout(panelLignes, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelLignes);
        add(scrollPane, BorderLayout.CENTER);

        // ✅ Zone inférieure complète
        JPanel bas = new JPanel(new BorderLayout());
        bas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        labelResume = new JLabel("Total : 0.00 €", SwingConstants.CENTER);
        labelResume.setFont(new Font("SansSerif", Font.BOLD, 15));
        bas.add(labelResume, BorderLayout.NORTH);

        JPanel boutonsBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        boutonAccueil = new JButton("🏠 Accueil");
        boutonRetour = new JButton("⬅ Retour catalogue");
        boutonVider = new JButton("🗑️ Vider le panier");
        boutonValider = new JButton("✅ Valider la commande");

        boutonsBas.add(boutonAccueil);
        boutonsBas.add(boutonRetour);
        boutonsBas.add(boutonVider);
        boutonsBas.add(boutonValider);

        bas.add(boutonsBas, BorderLayout.SOUTH);

        add(bas, BorderLayout.SOUTH);

        // 🔘 Actions boutons
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
        boutonRetour.addActionListener(e -> mainWindow.switchTo("catalogue"));
        boutonVider.addActionListener(e -> {
            mainWindow.getPanier().viderPanier();
            rafraichir();
        });
        boutonValider.addActionListener(this::validerCommande);

        rafraichir();
    }

    public void rafraichir() {
        panelLignes.removeAll();
        Panier panier = mainWindow.getPanier();
        List<LignePanier> lignes = panier.getLignes();
        ProduitDAO produitDAO = new ProduitDAO();

        for (LignePanier ligne : lignes) {
            JPanel lignePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lignePanel.setBorder(BorderFactory.createTitledBorder(ligne.getNomProduit()));

            // 🖱️ Clique pour voir le produit
            lignePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lignePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Produit produitClique = produitDAO.getProduitParId(ligne.getIdProduit());
                    if (produitClique != null) {
                        VueDetailProduit vue = new VueDetailProduit(mainWindow, produitClique, true);
                        vue.setVisible(true);
                    }
                }
            });

            Produit p = produitDAO.getProduitParId(ligne.getIdProduit());
            if (p != null && p.getImage() != null) {
                try {
                    ImageIcon icon = new ImageIcon(p.getImage());
                    Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    lignePanel.add(new JLabel(new ImageIcon(img)));
                } catch (Exception e) {
                    lignePanel.add(new JLabel("[Image]"));
                }
            }

            JLabel prixLabel = new JLabel(String.format("%.2f €", ligne.getPrix()));
            JTextField quantiteField = new JTextField(String.valueOf(ligne.getQuantite()), 3);
            JButton boutonMoins = new JButton("-");
            JButton boutonPlus = new JButton("+");
            JButton boutonMaj = new JButton("Mettre à jour");

            int idProduit = ligne.getIdProduit();

            boutonMoins.addActionListener(e -> {
                int quantite = Integer.parseInt(quantiteField.getText());
                if (quantite <= 1) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous supprimer ce produit du panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        panier.retirerProduit(idProduit);
                    }
                } else {
                    panier.modifierQuantite(idProduit, quantite - 1);
                }
                rafraichir();
            });

            boutonPlus.addActionListener(e -> {
                int quantite = Integer.parseInt(quantiteField.getText());
                panier.modifierQuantite(idProduit, quantite + 1);
                rafraichir();
            });

            boutonMaj.addActionListener(e -> {
                try {
                    int qte = Integer.parseInt(quantiteField.getText());
                    if (qte < 0) {
                        JOptionPane.showMessageDialog(this, "Quantité invalide.");
                        return;
                    }
                    if (qte == 0) {
                        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous supprimer ce produit du panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            panier.retirerProduit(idProduit);
                        }
                    } else {
                        panier.modifierQuantite(idProduit, qte);
                    }
                    rafraichir();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Entrez un nombre entier valide.");
                }
            });

            lignePanel.add(prixLabel);
            lignePanel.add(boutonMoins);
            lignePanel.add(quantiteField);
            lignePanel.add(boutonPlus);
            lignePanel.add(boutonMaj);
            lignePanel.add(new JLabel("Sous-total : " + String.format("%.2f €", ligne.getSousTotal())));

            panelLignes.add(lignePanel);
        }

        double sousTotal = panier.calculerTotalBrut();
        Utilisateur u = mainWindow.getUtilisateurConnecte();
        double remise = (u != null && u.isClientFidele()) ? sousTotal * 0.1 : 0.0;
        double total = sousTotal - remise;

        labelResume.setText(
                "<html><div style='text-align:center'>"
                        + "Sous-total : " + String.format("%.2f", sousTotal) + " €<br>"
                        + "Remise fidélité : -" + String.format("%.2f", remise) + " €<br>"
                        + "<b>Total à payer : " + String.format("%.2f", total) + " €</b></div></html>"
        );

        panelLignes.revalidate();
        panelLignes.repaint();
    }

    private void validerCommande(ActionEvent e) {
        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour valider la commande.");
            mainWindow.switchTo("connexion");
            return;
        }

        if (mainWindow.getPanier().estVide()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide !");
            return;
        }

        // Redirection vers la vue Paiement
        mainWindow.switchTo("paiement");
    }



}
