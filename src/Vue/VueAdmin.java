package Vue;

import DAO.ProduitDAO;
import modele.Produit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class VueAdmin extends JPanel {

    private MainWindow mainWindow;
    private JTable tableProduits;
    private DefaultTableModel model;
    private JButton boutonAjouter;
    private JButton boutonSupprimer;
    private JButton boutonAccueil;

    public VueAdmin(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("👨‍💼 Interface Administrateur - Gestion des Produits", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        // Table produits
        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Prix (€)", "Stock"}, 0);
        tableProduits = new JTable(model);
        add(new JScrollPane(tableProduits), BorderLayout.CENTER);

        // Listener double-clic pour ouvrir VueDetailProduit
        tableProduits.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tableProduits.getSelectedRow();
                    if (row != -1) {
                        int id = (int) model.getValueAt(row, 0);
                        String nom = (String) model.getValueAt(row, 1);
                        double prix = Double.parseDouble(model.getValueAt(row, 2).toString());
                        int stock = Integer.parseInt(model.getValueAt(row, 3).toString());

                        Produit produit = new Produit(id, nom, prix, stock);
                        new VueDetailProduit(produit).setVisible(true);
                    }
                }
            }
        });

        // Bas : boutons
        JPanel bas = new JPanel(new FlowLayout());
        boutonAjouter = new JButton("Ajouter un produit");
        boutonSupprimer = new JButton("Supprimer sélection");
        boutonAccueil = new JButton("Accueil");

        bas.add(boutonAjouter);
        bas.add(boutonSupprimer);
        bas.add(boutonAccueil);

        add(bas, BorderLayout.SOUTH);

        // Actions
        boutonAjouter.addActionListener(e -> ajouterProduit());
        boutonSupprimer.addActionListener(e -> supprimerProduit());
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));

        chargerProduits();
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
                    p.getQuantiteStock()
            });
        }
    }

    private void ajouterProduit() {
        String nom = JOptionPane.showInputDialog(this, "Nom du produit :");
        if (nom == null || nom.isBlank()) return;

        String prixStr = JOptionPane.showInputDialog(this, "Prix (€) :");
        String stockStr = JOptionPane.showInputDialog(this, "Stock :");

        try {
            double prix = Double.parseDouble(prixStr);
            int stock = Integer.parseInt(stockStr);

            // --- Choisir l'image PNG ---
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images PNG", "png"));
            int result = fileChooser.showOpenDialog(this);

            String cheminImage = "";

            if (result == JFileChooser.APPROVE_OPTION) {
                File imageSource = fileChooser.getSelectedFile();
                String nomFichier = imageSource.getName();
                File destination = new File("Vue/images/" + nomFichier);

                try {
                    java.nio.file.Files.copy(
                            imageSource.toPath(),
                            destination.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );
                    cheminImage = "Vue/images/" + nomFichier;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la copie de l'image.");
                    ex.printStackTrace();
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Ajout annulé : aucune image sélectionnée.");
                return;
            }

            // --- Création et ajout du produit ---
            Produit produit = new Produit(0, nom, prix, stock, cheminImage);
            ProduitDAO dao = new ProduitDAO();
            boolean ok = dao.ajouterProduit(produit);

            if (ok) {
                chargerProduits();
                JOptionPane.showMessageDialog(this, "Produit ajouté !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur d'ajout.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Entrées invalides.");
        }
    }


    private void supprimerProduit() {
        int row = tableProduits.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un produit à supprimer.");
            return;
        }

        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer suppression ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            ProduitDAO dao = new ProduitDAO();
            boolean ok = dao.supprimerProduit(id);

            if (ok) {
                chargerProduits();
                JOptionPane.showMessageDialog(this, "Produit supprimé.");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur de suppression.");
            }
        }
    }
}
