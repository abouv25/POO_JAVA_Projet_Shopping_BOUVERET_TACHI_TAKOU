package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VueHistoriqueFactures extends JFrame {

    private JTable tableFactures;
    private Utilisateur utilisateur;
    private JButton boutonVoirDetail;

    public VueHistoriqueFactures(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Historique des Factures");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tableFactures = new JTable();
        chargerFactures();

        tableFactures.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // double-clic
                    ouvrirDetail();
                }
            }
        });

        add(new JScrollPane(tableFactures), BorderLayout.CENTER);

        boutonVoirDetail = new JButton("Voir les détails");
        boutonVoirDetail.addActionListener(e -> ouvrirDetail());
        add(boutonVoirDetail, BorderLayout.SOUTH);
    }

    private void chargerFactures() {
        FactureDAO dao = new FactureDAO();
        List<Facture> factures = dao.listerFacturesPourUtilisateur(utilisateur);

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
        new VueDetailFacture(idFacture,utilisateur).setVisible(true);
    }
}
