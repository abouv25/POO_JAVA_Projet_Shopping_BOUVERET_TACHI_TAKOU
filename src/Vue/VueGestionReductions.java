package Vue;

import DAO.ReductionDAO;
import modele.Reduction;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueGestionReductions extends JPanel {

    private final MainWindow mainWindow;
    private final JPanel panelListe;

    public VueGestionReductions(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Panneau haut avec bouton ajouter
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        StyleUI.appliquerFondEtCadre(haut);

        JButton boutonAjouter = new JButton("➕ Ajouter Réduction");
        StyleUI.styliserBouton(boutonAjouter);
        boutonAjouter.addActionListener(e -> ouvrirFenetreAjout());

        haut.add(boutonAjouter);
        add(haut, BorderLayout.NORTH);

        // ✅ Zone centrale : liste réductions
        panelListe = new JPanel();
        panelListe.setLayout(new BoxLayout(panelListe, BoxLayout.Y_AXIS));
        panelListe.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelListe.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelListe);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        rafraichirListe();
    }

    public void rafraichirListe() {
        panelListe.removeAll();

        List<Reduction> reductions = new ReductionDAO().listerReductions();
        if (reductions.isEmpty()) {
            JLabel videLabel = new JLabel("Aucune réduction enregistrée.", SwingConstants.CENTER);
            videLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
            videLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListe.add(videLabel);
        } else {
            for (Reduction r : reductions) {
                JPanel carte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
                carte.setBorder(BorderFactory.createTitledBorder("Réduction de " + r.getPourcentage() + " %"));
                StyleUI.appliquerFondEtCadre(carte);

                JLabel pourcentageLabel = new JLabel(r.getPourcentage() + " %");
                JButton modifier = new JButton("✏️ Modifier");
                JButton supprimer = new JButton("🗑️ Supprimer");

                StyleUI.styliserBouton(modifier);
                StyleUI.styliserBouton(supprimer);

                modifier.addActionListener(e -> modifierReduction(r));
                supprimer.addActionListener(e -> supprimerReduction(r));

                carte.add(pourcentageLabel);
                carte.add(modifier);
                carte.add(supprimer);

                panelListe.add(carte);
                panelListe.add(Box.createVerticalStrut(10)); // Petit espace entre les cartes
            }
        }

        panelListe.revalidate();
        panelListe.repaint();
    }

    private void ouvrirFenetreAjout() {
        VueCreationReduction creation = new VueCreationReduction(mainWindow);
        creation.setVisible(true);
    }

    private void modifierReduction(Reduction r) {
        String input = JOptionPane.showInputDialog(this, "Nouveau pourcentage :", r.getPourcentage());
        if (input != null) {
            try {
                double nouveau = Double.parseDouble(input);
                if (nouveau <= 0 || nouveau >= 100) {
                    JOptionPane.showMessageDialog(this, "Pourcentage invalide.");
                    return;
                }
                r.setPourcentage(nouveau);
                boolean ok = new ReductionDAO().modifierReduction(r);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Réduction modifiée !");
                    rafraichirListe();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur de modification.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nombre invalide.");
            }
        }
    }

    private void supprimerReduction(Reduction r) {
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette réduction ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = new ReductionDAO().supprimerReduction(r.getId());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Réduction supprimée.");
                rafraichirListe();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur de suppression.");
            }
        }
    }
}
