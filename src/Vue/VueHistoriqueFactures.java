package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

        // ✅ Chargement initial
        rechargerFactures();
    }

    private void rechargerFactures() {
        removeAll();
        setLayout(new BorderLayout());

        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        checkBox2025 = new JCheckBox("Afficher uniquement 2025");
        triComboBox = new JComboBox<>(new String[]{
                "Aucun tri", "Tri par Montant croissant", "Tri par Date décroissante"
        });
        topPanel.add(checkBox2025);
        topPanel.add(triComboBox);
        add(topPanel, BorderLayout.NORTH);

        JPanel bas = new JPanel(new BorderLayout());
        boutonRetour = new JButton("⬅️ Page précédente");
        boutonVoirDetail = new JButton("Voir les détails");
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());
        boutonVoirDetail.addActionListener(e -> ouvrirDetail());
        bas.add(boutonRetour, BorderLayout.WEST);
        bas.add(boutonVoirDetail, BorderLayout.EAST);
        add(bas, BorderLayout.SOUTH);

        dao = new FactureDAO();

        if (utilisateur == null) {
            add(new JLabel("Erreur : utilisateur non connecté", SwingConstants.CENTER), BorderLayout.CENTER);
            revalidate(); repaint();
            return;
        }

        List<Facture> factures = dao.listerFacturesPourUtilisateur(utilisateur);

        if (checkBox2025.isSelected()) {
            factures = factures.stream()
                    .filter(f -> f.getDate().getYear() == 2025)
                    .collect(Collectors.toList());
        }

        String triChoisi = (String) triComboBox.getSelectedItem();
        if (triChoisi != null) {
            switch (triChoisi) {
                case "Tri par Montant croissant":
                    factures.sort(Comparator.comparingDouble(Facture::getMontantTotal));
                    break;
                case "Tri par Date décroissante":
                    factures.sort(Comparator.comparing(Facture::getDate).reversed());
                    break;
            }
        }

        if (factures.isEmpty()) {
            add(new JLabel("Aucune facture trouvée.", SwingConstants.CENTER), BorderLayout.CENTER);
        } else {
            tableFactures = new JTable();
            chargerFacturesDansTableau(factures);
            tableFactures.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) ouvrirDetail();
                }
            });
            add(new JScrollPane(tableFactures), BorderLayout.CENTER);
        }

        revalidate();
        repaint();
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
        mainWindow.chargerVueDetailFacture(idFacture, utilisateur);
    }
}
