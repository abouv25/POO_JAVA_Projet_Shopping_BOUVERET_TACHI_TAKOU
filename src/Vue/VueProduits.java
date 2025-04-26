// package et imports identiques
package Vue;

import DAO.ProduitDAO;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class VueProduits extends JPanel {

    private MainWindow mainWindow;
    private Utilisateur utilisateur;
    private JPanel panelCartes;
    private JScrollPane scrollPane;

    private JTextField champRecherche;
    private JTextField champPrixMin;
    private JTextField champPrixMax;
    private JCheckBox checkBoxStock;
    private JList<String> suggestionList;
    private JScrollPane suggestionPane;

    private List<Produit> tousLesProduits;

    public VueProduits(MainWindow mainWindow, Utilisateur utilisateur) {
        this.mainWindow = mainWindow;
        this.utilisateur = utilisateur;

        setLayout(new BorderLayout());
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // 🔍 Filtres à gauche
        JPanel filtres = new JPanel();
        filtres.setLayout(new BoxLayout(filtres, BoxLayout.Y_AXIS));
        filtres.setBorder(BorderFactory.createTitledBorder("🔍 Filtres"));
        filtres.setPreferredSize(new Dimension(200, 0));

        champRecherche = new JTextField();
        champPrixMin = new JTextField();
        champPrixMax = new JTextField();
        checkBoxStock = new JCheckBox("En stock uniquement");
        JButton boutonFiltrer = new JButton("Appliquer les filtres");
        JButton boutonReset = new JButton("Réinitialiser");

        filtres.add(new JLabel("Nom du produit :"));
        filtres.add(champRecherche);

        suggestionList = new JList<>();
        suggestionPane = new JScrollPane(suggestionList);
        suggestionPane.setPreferredSize(new Dimension(180, 80));
        suggestionPane.setVisible(false);
        filtres.add(suggestionPane);

        filtres.add(Box.createVerticalStrut(10));
        filtres.add(new JLabel("Prix minimum :"));
        filtres.add(champPrixMin);
        filtres.add(Box.createVerticalStrut(10));
        filtres.add(new JLabel("Prix maximum :"));
        filtres.add(champPrixMax);
        filtres.add(Box.createVerticalStrut(10));
        filtres.add(checkBoxStock);
        filtres.add(Box.createVerticalStrut(10));
        filtres.add(boutonFiltrer);
        filtres.add(Box.createVerticalStrut(5));
        filtres.add(boutonReset);

        add(filtres, BorderLayout.WEST);

        // Centre = cartes produits
        panelCartes = new JPanel(new GridLayout(0, 3, 20, 20));
        panelCartes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelCartes.setBackground(new Color(250, 250, 250));

        scrollPane = new JScrollPane(panelCartes);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Actions
        boutonFiltrer.addActionListener(e -> appliquerFiltres());

        boutonReset.addActionListener(e -> {
            champRecherche.setText("");
            champPrixMin.setText("");
            champPrixMax.setText("");
            checkBoxStock.setSelected(false);
            suggestionPane.setVisible(false);
            afficherProduits(tousLesProduits);
        });

        champRecherche.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                String input = champRecherche.getText().trim().toLowerCase();
                if (input.isEmpty()) {
                    suggestionPane.setVisible(false);
                    return;
                }
                List<String> suggestions = tousLesProduits.stream()
                        .map(p -> p.getNom())
                        .filter(n -> n.toLowerCase().contains(input))
                        .limit(10)
                        .collect(Collectors.toList());
                if (suggestions.isEmpty()) {
                    suggestionPane.setVisible(false);
                } else {
                    suggestionList.setListData(suggestions.toArray(new String[0]));
                    suggestionPane.setVisible(true);
                }
            }
        });

        suggestionList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!suggestionList.isSelectionEmpty()) {
                    champRecherche.setText(suggestionList.getSelectedValue());
                    suggestionPane.setVisible(false);
                    appliquerFiltres();
                }
            }
        });

        chargerProduits();
    }

    private void chargerProduits() {
        ProduitDAO dao = new ProduitDAO();
        tousLesProduits = dao.listerProduits();
        afficherProduits(tousLesProduits);
    }

    private void appliquerFiltres() {
        String recherche = champRecherche.getText().trim().toLowerCase();
        String minStr = champPrixMin.getText().trim();
        String maxStr = champPrixMax.getText().trim();
        boolean filtrerStock = checkBoxStock.isSelected();

        try {
            final double min = minStr.isEmpty() ? 0 : Double.parseDouble(minStr);
            final double max = maxStr.isEmpty() ? Double.MAX_VALUE : Double.parseDouble(maxStr);

            List<Produit> filtres = tousLesProduits.stream()
                    .filter(p -> p.getNom().toLowerCase().contains(recherche))
                    .filter(p -> p.getPrix() >= min && p.getPrix() <= max)
                    .filter(p -> !filtrerStock || p.getQuantiteStock() > 0)
                    .collect(Collectors.toList());

            afficherProduits(filtres);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Prix invalide.");
        }
    }


    private void afficherProduits(List<Produit> produits) {
        panelCartes.removeAll();
        for (Produit produit : produits) {
            panelCartes.add(creerCarteProduit(produit));
        }
        panelCartes.revalidate();
        panelCartes.repaint();
    }

    private JPanel creerCarteProduit(Produit produit) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setPreferredSize(new Dimension(200, 280));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

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

        JLabel nomLabel = new JLabel(produit.getNom(), SwingConstants.CENTER);
        nomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(nomLabel);

        JLabel prixLabel = new JLabel(String.format("%.2f €", produit.getPrix()), SwingConstants.CENTER);
        prixLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(prixLabel);

        JLabel stockLabel = new JLabel("Stock : " + produit.getQuantiteStock(), SwingConstants.CENTER);
        stockLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        stockLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        carte.add(stockLabel);

        carte.add(Box.createVerticalStrut(10));

        Produit produitFinal = produit; // 🔧 pour corriger l'erreur lambda

        JButton boutonAjouter = new JButton("Ajouter au panier");
        boutonAjouter.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonAjouter.addActionListener((ActionEvent e) -> {
            String input = JOptionPane.showInputDialog(this, "Quantité à ajouter :", "1");
            if (input == null) return;
            try {
                int qte = Integer.parseInt(input);
                if (qte <= 0) throw new NumberFormatException();
                mainWindow.getPanier().ajouterProduit(produitFinal.getId(), produitFinal.getNom(), produitFinal.getPrix(), qte);
                JOptionPane.showMessageDialog(this, qte + " x " + produitFinal.getNom() + " ajouté(s) au panier.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantité invalide.");
            }
        });

        JButton boutonVoir = new JButton("Voir le produit");
        boutonVoir.setAlignmentX(Component.CENTER_ALIGNMENT);
        boutonVoir.addActionListener(e -> {
            VueDetailProduit vue = new VueDetailProduit(produitFinal, true);
            vue.setVisible(true);
        });

        carte.add(boutonAjouter);
        carte.add(Box.createVerticalStrut(5));
        carte.add(boutonVoir);

        return carte;
    }
}
