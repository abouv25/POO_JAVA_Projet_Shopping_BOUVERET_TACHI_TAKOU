package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VueGestionUtilisateurs extends JPanel {
    private MainWindow mainWindow;
    private JTable table;
    private DefaultTableModel model;
    private JButton boutonPrecedent, boutonSuivant, boutonAjouter;
    private JLabel labelPage;
    private int pageActuelle = 1;
    private final int TAILLE_PAGE = 10;
    private List<Utilisateur> tousLesUtilisateurs;

    public VueGestionUtilisateurs(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Gestion des utilisateurs", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"ID", "Nom", "Email", "Rôle", "Fidèle"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);

        // ✅ Double clic = modifier utilisateur
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int id = (int) model.getValueAt(row, 0);
                        Utilisateur u = tousLesUtilisateurs.stream()
                                .filter(user -> user.getId() == id)
                                .findFirst().orElse(null);
                        if (u != null) {
                            ModifierUtilisateurDialog dialog = new ModifierUtilisateurDialog(mainWindow, u);
                            dialog.setVisible(true);
                            if (dialog.isModifie()) {
                                chargerUtilisateurs();
                            }
                        }
                    }
                }
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // --- Bas avec pagination et bouton ajout ---
        JPanel bas = new JPanel(new BorderLayout());

        boutonAjouter = new JButton("➕ Ajouter un utilisateur");
        boutonAjouter.addActionListener(e -> {
            Utilisateur nouvelUtilisateur = new Utilisateur();
            ModifierUtilisateurDialog dialog = new ModifierUtilisateurDialog(mainWindow, nouvelUtilisateur);
            dialog.setVisible(true);
            if (dialog.isModifie()) {
                chargerUtilisateurs();
            }
        });

        bas.add(boutonAjouter, BorderLayout.NORTH);

        JPanel pagination = new JPanel(new FlowLayout());
        boutonPrecedent = new JButton("Préc");
        boutonSuivant = new JButton("Suiv");
        labelPage = new JLabel();

        pagination.add(boutonPrecedent);
        pagination.add(labelPage);
        pagination.add(boutonSuivant);

        boutonPrecedent.addActionListener(e -> {
            if (pageActuelle > 1) {
                pageActuelle--;
                chargerUtilisateurs();
            }
        });

        boutonSuivant.addActionListener(e -> {
            int totalPages = (int) Math.ceil((double) tousLesUtilisateurs.size() / TAILLE_PAGE);
            if (pageActuelle < totalPages) {
                pageActuelle++;
                chargerUtilisateurs();
            }
        });

        bas.add(pagination, BorderLayout.SOUTH);
        add(bas, BorderLayout.SOUTH);

        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        UtilisateurDAO dao = new UtilisateurDAO();
        tousLesUtilisateurs = dao.listerUtilisateurs();

        model.setRowCount(0);
        int start = (pageActuelle - 1) * TAILLE_PAGE;
        int end = Math.min(start + TAILLE_PAGE, tousLesUtilisateurs.size());
        List<Utilisateur> page = tousLesUtilisateurs.subList(start, end);

        for (Utilisateur u : page) {
            String role = u.isEstAdmin() ? "<html><span style='color: red;'>Admin</span></html>"
                    : "<html><span style='color: green;'>Client</span></html>";
            String fidelite = u.isClientFidele() ? "<html><span style='color: blue;'>Oui</span></html>" : "Non";

            model.addRow(new Object[]{
                    u.getId(),
                    u.getNom(),
                    u.getEmail(),
                    role,
                    fidelite
            });
        }

        int totalPages = (int) Math.ceil((double) tousLesUtilisateurs.size() / TAILLE_PAGE);
        labelPage.setText("Page " + pageActuelle + " / " + totalPages);
    }
}
