package Vue;

import modele.LignePanier;
import modele.Panier;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class VuePanier extends JPanel {

    private MainWindow mainWindow;
    private JPanel panelLignes;
    private JLabel labelTotal;
    private JButton boutonValider, boutonVider, boutonRetour, boutonAccueil;

    public VuePanier(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        // ✅ Barre supérieure avec logo + utilisateur
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // Panel principal
        panelLignes = new JPanel();
        panelLignes.setLayout(new BoxLayout(panelLignes, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelLignes);
        add(scrollPane, BorderLayout.CENTER);

        // Total
        labelTotal = new JLabel("Total : 0.00 €", SwingConstants.CENTER);
        add(labelTotal, BorderLayout.SOUTH);

        // Bas
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

        // Actions globales
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

        for (LignePanier ligne : lignes) {
            JPanel lignePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            lignePanel.setBorder(BorderFactory.createTitledBorder(ligne.getNomProduit()));

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

        labelTotal.setText("Total : " + String.format("%.2f", panier.calculerTotalBrut()) + " €");
        panelLignes.revalidate();
        panelLignes.repaint();
    }

    private void validerCommande(ActionEvent e) {
        Utilisateur utilisateur = mainWindow.getUtilisateurConnecte();

        if (utilisateur == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez vous connecter pour valider votre panier.",
                    "Connexion requise",
                    JOptionPane.WARNING_MESSAGE);
            mainWindow.switchTo("connexion");
            return;
        }

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
