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
    private JButton boutonRetour;
    private JCheckBox checkBox2025;
    private JComboBox<String> triComboBox;
    private FactureDAO dao;
    private MainWindow mainWindow;

    public VueHistoriqueFactures(Utilisateur utilisateur, MainWindow mainWindow) {
        this.utilisateur = utilisateur;
        this.mainWindow = mainWindow;
        this.dao = new FactureDAO();

        setLayout(new BorderLayout());

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Panneau de filtrage
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBox2025 = new JCheckBox("Afficher uniquement 2025");
        triComboBox = new JComboBox<>(new String[]{
                "Aucun tri", "Tri par Montant croissant", "Tri par Date décroissante"
        });
        topPanel.add(checkBox2025);
        topPanel.add(triComboBox);
        add(topPanel, BorderLayout.NORTH);

        // ✅ Table centrale
        tableFactures = new JTable();
        add(new JScrollPane(tableFactures), BorderLayout.CENTER);

        // ✅ Bas de page avec deux boutons
        JPanel bas = new JPanel(new BorderLayout());
        boutonRetour = new JButton("⬅️ Page précédente");
        boutonVoirDetail = new JButton("Voir les détails");

        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());
        boutonVoirDetail.addActionListener(e -> ouvrirDetail());

        bas.add(boutonRetour, BorderLayout.WEST);
        bas.add(boutonVoirDetail, BorderLayout.EAST);
        add(bas, BorderLayout.SOUTH);

        // ✅ Double clic sur tableau
        tableFactures.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    ouvrirDetail();
                }
            }
        });

        // ✅ Listeners de filtrage/tri
        checkBox2025.addActionListener(e -> rechargerFactures());
        triComboBox.addActionListener(e -> rechargerFactures());

        rechargerFactures();
    }

    private void rechargerFactures() {
        List<Facture> factures = dao.listerFacturesPourUtilisateur(utilisateur);

        // ✅ Filtre 2025
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

        // ✅ Tri
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
