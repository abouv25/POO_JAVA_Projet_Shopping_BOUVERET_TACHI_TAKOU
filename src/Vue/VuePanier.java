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

    private MainWindow mainWindow;
    private JPanel panelLignes;
    private JLabel labelResume;
    private JButton boutonValider, boutonVider, boutonRetour, boutonAccueil;

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

        // ✅ Zone inférieure : résumé + boutons
        labelResume = new JLabel("Total : 0.00 €", SwingConstants.CENTER);
        labelResume.setFont(new Font("SansSerif", Font.BOLD, 15));
        add(labelResume, BorderLayout.SOUTH);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        boutonAccueil = new JButton("Accueil");
        boutonRetour = new JButton("Retour catalogue");
        boutonVider = new JButton("Vider le panier");
        boutonValider = new JButton("Valider la commande");

        bas.add(boutonAccueil);
        bas.add(boutonRetour);
        bas.add(boutonVider);
        bas.add(boutonValider);
        add(bas, BorderLayout.PAGE_END);

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

            lignePanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            lignePanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    Produit produitClique = produitDAO.getProduitParId(ligne.getIdProduit());
                    if (produitClique != null) {
                        VueDetailProduit vue = new VueDetailProduit(produitClique, true);
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
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Voulez-vous supprimer ce produit du panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);
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
                        int confirm = JOptionPane.showConfirmDialog(this,
                                "Voulez-vous supprimer ce produit du panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);
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
        Panier panier = mainWindow.getPanier();
        List<LignePanier> lignes = panier.getLignes();

        if (lignes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Votre panier est vide.");
            return;
        }

        Utilisateur client = mainWindow.getUtilisateurConnecte();
        if (client == null) {
            JOptionPane.showMessageDialog(this, "Vous devez être connecté pour valider une commande.");
            return;
        }

        Facture facture = new Facture(client, lignes);
        FactureDAO dao = new FactureDAO();

        boolean ok = dao.ajouterFacture(facture);
        if (!ok) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de la facture.");
            return;
        }

        // ✅ Choix de l'emplacement pour enregistrer le PDF
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer la facture PDF");
        fileChooser.setSelectedFile(new File("facture_" + facture.getId() + ".pdf"));

        int choix = fileChooser.showSaveDialog(this);
        if (choix != JFileChooser.APPROVE_OPTION) return;

        File fichier = fileChooser.getSelectedFile();
        String chemin = fichier.getAbsolutePath();
        if (!chemin.toLowerCase().endsWith(".pdf")) chemin += ".pdf";

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new java.io.FileOutputStream(chemin));
            doc.open();

            Paragraph titre = new Paragraph("FACTURE", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD));
            titre.setAlignment(Element.ALIGN_CENTER);
            doc.add(titre);

            doc.add(new Paragraph("Numéro de facture : " + facture.getId()));
            doc.add(new Paragraph("Date : " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            doc.add(new Paragraph("Client : " + client.getNom()));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Produit");
            table.addCell("Prix unitaire");
            table.addCell("Quantité");
            table.addCell("Sous-total");

            for (LignePanier l : lignes) {
                table.addCell(l.getNomProduit());
                table.addCell(String.format("%.2f €", l.getPrix()));
                table.addCell(String.valueOf(l.getQuantite()));
                table.addCell(String.format("%.2f €", l.getPrix() * l.getQuantite()));
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total à payer : " + String.format("%.2f €", facture.getMontantTotal())));

            doc.add(new Paragraph(" "));
            Paragraph merci = new Paragraph("Merci de votre commande !", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.ITALIC));
            merci.setAlignment(Element.ALIGN_CENTER);
            doc.add(merci);

            doc.close();

            // ✅ Réinitialiser le panier
            mainWindow.getPanier().viderPanier();
            rafraichir();

            JOptionPane.showMessageDialog(this, "Commande validée ! PDF enregistré à :\n" + chemin);
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(chemin));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur génération PDF : " + ex.getMessage());
        }
    }
}
