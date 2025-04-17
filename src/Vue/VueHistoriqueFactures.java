package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class VueHistoriqueFactures extends JPanel {

    private JTable tableFactures;
    private Utilisateur utilisateur;
    private JButton boutonVoirDetail;
    private JCheckBox checkBox2025;
    private JButton boutonAccueil;
    private JComboBox<String> triComboBox;
    private FactureDAO dao;
    private MainWindow mainWindow;

    public VueHistoriqueFactures(Utilisateur utilisateur, MainWindow mainWindow) {
        this.utilisateur = utilisateur;
        this.mainWindow = mainWindow;
        this.dao = new FactureDAO();

        setLayout(new BorderLayout());

        tableFactures = new JTable();

        // --- Panneau du haut avec filtre, tri et bouton accueil ---
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBox2025 = new JCheckBox("Afficher uniquement 2025");
        boutonAccueil = new JButton("Revenir à l’accueil");
        triComboBox = new JComboBox<>(new String[]{
                "Aucun tri", "Tri par Montant croissant", "Tri par Date décroissante"
        });

        topPanel.add(checkBox2025);
        topPanel.add(triComboBox);
        topPanel.add(boutonAccueil);
        add(topPanel, BorderLayout.NORTH);

        // Listeners
        checkBox2025.addActionListener(e -> rechargerFactures());
        triComboBox.addActionListener(e -> rechargerFactures());
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));

        // --- Tableau central ---
        add(new JScrollPane(tableFactures), BorderLayout.CENTER);

        // --- Bouton de détail ---
        boutonVoirDetail = new JButton("Voir les détails");
        boutonVoirDetail.addActionListener(e -> ouvrirDetail());
        add(boutonVoirDetail, BorderLayout.SOUTH);

        tableFactures.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ouvrirDetail();
                }
            }
        });

        rechargerFactures(); // Chargement initial
    }

    private void rechargerFactures() {
        List<Facture> factures = dao.listerFacturesPourUtilisateur(utilisateur);

        // Filtrage
        if (checkBox2025.isSelected()) {
            factures = factures.stream()
                    .filter(f -> {
                        try {
                            LocalDate date = LocalDate.parse(f.getDateFormatee());
                            return date.getYear() == 2025;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        }

        // Tri
        String triChoisi = (String) triComboBox.getSelectedItem();
        if (triChoisi != null) {
            switch (triChoisi) {
                case "Tri par Montant croissant":
                    factures.sort(Comparator.comparingDouble(Facture::getMontantTotal));
                    break;
                case "Tri par Date décroissante":
                    factures.sort(Comparator.comparing(Facture::getDateFormatee).reversed());
                    break;
            }
        }

        chargerFacturesDansTableau(factures);
    }

    private void chargerFacturesDansTableau(List<Facture> factures) {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"ID", "Date", "Montant Total (€)", "Remise (%)"}, 0);

        for (Facture f : factures) {
            model.addRow(new Object[]{
                    f.getId(),
                    f.getDateFormatee(),
                    String.format("%.2f", f.getMontantTotal()),
                    String.format("%.0f", f.getRemisePourcent())
            });
        }

        tableFactures.setModel(model);
    }

    private void ouvrirDetail() {
        int row = tableFactures.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une facture.");
            return;
        }

        int idFacture = (int) tableFactures.getValueAt(row, 0);

        VueDetailFacture vueDetail = new VueDetailFacture(idFacture, utilisateur, mainWindow);
        mainWindow.ajouterVue("detailFacture", vueDetail);
        mainWindow.switchTo("detailFacture");
    }
}
