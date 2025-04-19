package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VueHistoriqueCommandes extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField champRecherche;
    private JButton boutonRetour, boutonToutVoir;
    private List<Facture> factures;
    private TableRowSorter<DefaultTableModel> sorter;

    public VueHistoriqueCommandes(MainWindow mainWindow) {
        setLayout(new BorderLayout());

        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u == null) {
            JOptionPane.showMessageDialog(this, "Veuillez vous connecter.");
            mainWindow.switchTo("connexion");
            return;
        }

        JLabel titre = new JLabel("\uD83D\uDCDC Historique de vos commandes", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        // Centre : tableau des factures
        model = new DefaultTableModel(new Object[]{"Date", "N° Facture", "Montant total", "Remise", "Fidèle"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int idFacture = Integer.parseInt(table.getValueAt(row, 1).toString());
                        VueDetailFacture vue = new VueDetailFacture(idFacture, u, mainWindow);
                        mainWindow.ajouterVue("detailFacture", vue);
                        mainWindow.switchTo("detailFacture");
                    }
                }
            }
        });

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                double montant = Double.parseDouble(model.getValueAt(table.convertRowIndexToModel(row), 2).toString().replace(" €", ""));
                if (montant > 100) {
                    c.setBackground(new Color(220, 255, 220));
                } else if (montant < 10) {
                    c.setBackground(new Color(245, 245, 245));
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bas : champ de recherche + boutons
        JPanel bas = new JPanel(new BorderLayout());

        champRecherche = new JTextField(10);
        champRecherche.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrer(); }
        });

        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.add(new JLabel("🔎 N° Facture : "));
        recherchePanel.add(champRecherche);

        boutonToutVoir = new JButton("Tout voir");
        boutonToutVoir.addActionListener(e -> champRecherche.setText(""));
        recherchePanel.add(boutonToutVoir);

        boutonRetour = new JButton("⬅ Retour");
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        droite.add(boutonRetour);

        bas.add(recherchePanel, BorderLayout.WEST);
        bas.add(droite, BorderLayout.EAST);

        add(bas, BorderLayout.SOUTH);

        chargerFactures(u);
    }

    private void chargerFactures(Utilisateur u) {
        FactureDAO dao = new FactureDAO();
        factures = dao.listerFacturesPourUtilisateur(u);

        model.setRowCount(0);

        for (Facture f : factures) {
            String remise = String.format("%.2f €", f.getRemisePourcent() * f.calculerTotal() / 100);
            String badge = f.getRemisePourcent() > 0 ? "✅" : "-";
            model.addRow(new Object[]{
                    f.getDateFormatee(),
                    f.getId(),
                    String.format("%.2f €", f.getMontantTotal()),
                    remise,
                    badge
            });
        }
    }

    private void filtrer() {
        RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + champRecherche.getText(), 1);
        sorter.setRowFilter(rf);
    }
}
