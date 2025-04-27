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

        // ✅ Haut : bouton ajouter
        JPanel haut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        haut.setOpaque(false);

        JButton boutonAjouter = new JButton("➕ Ajouter une réduction");
        StyleUI.styliserBouton(boutonAjouter);
        boutonAjouter.addActionListener(e -> ouvrirFenetreAjout());

        haut.add(boutonAjouter);
        add(haut, BorderLayout.NORTH);

        // ✅ Centre : liste réductions
        panelListe = new JPanel();
        panelListe.setLayout(new BoxLayout(panelListe, BoxLayout.Y_AXIS));
        panelListe.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelListe.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(panelListe);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // ✅ Bas : bouton retour
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bas.setOpaque(false);

        JButton boutonRetour = new JButton("⬅ Retour");
        StyleUI.styliserBouton(boutonRetour);
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());

        bas.add(boutonRetour);
        add(bas, BorderLayout.SOUTH);

        rafraichirListe();
    }

    public void rafraichirListe() {
        panelListe.removeAll();

        List<Reduction> reductions = new ReductionDAO().listerReductions();
        if (reductions.isEmpty()) {
            JLabel vide = new JLabel("Aucune réduction enregistrée.", SwingConstants.CENTER);
            vide.setFont(new Font("SansSerif", Font.ITALIC, 14));
            vide.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelListe.add(vide);
        } else {
            for (Reduction r : reductions) {
                JPanel carte = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
                carte.setBorder(BorderFactory.createTitledBorder(r.getNom()));
                StyleUI.appliquerFondEtCadre(carte);

                JLabel pourcentage = new JLabel(r.getPourcentage() + " %");
                pourcentage.setFont(new Font("SansSerif", Font.BOLD, 14));

                JButton modifier = new JButton("✏️ Modifier");
                JButton supprimer = new JButton("🗑️ Supprimer");

                StyleUI.styliserBouton(modifier);
                StyleUI.styliserBouton(supprimer);

                modifier.addActionListener(e -> modifierReduction(r));
                supprimer.addActionListener(e -> supprimerReduction(r));

                carte.add(pourcentage);
                carte.add(modifier);
                carte.add(supprimer);

                panelListe.add(carte);
                panelListe.add(Box.createVerticalStrut(10));
            }
        }

        panelListe.revalidate();
        panelListe.repaint();
    }

    private void ouvrirFenetreAjout() {
        JTextField champNom = new JTextField();
        JTextField champPourcentage = new JTextField();
        Object[] champs = {
                "Nom de la réduction :", champNom,
                "Pourcentage de réduction :", champPourcentage
        };

        int option = JOptionPane.showConfirmDialog(this, champs, "Ajouter Réduction", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = champNom.getText().trim();
                double pourcentage = Double.parseDouble(champPourcentage.getText().trim());

                if (nom.isEmpty() || pourcentage <= 0 || pourcentage >= 100) {
                    JOptionPane.showMessageDialog(this, "Nom ou pourcentage invalide.");
                    return;
                }

                Reduction r = new Reduction();
                r.setNom(nom);
                r.setPourcentage(pourcentage);

                boolean ok = new ReductionDAO().ajouterReduction(r);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Réduction ajoutée !");
                    rafraichirListe();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur ajout.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nombre invalide.");
            }
        }
    }

    private void modifierReduction(Reduction r) {
        JTextField champNom = new JTextField(r.getNom());
        JTextField champPourcentage = new JTextField(String.valueOf(r.getPourcentage()));
        Object[] champs = {
                "Nom de la réduction :", champNom,
                "Nouveau pourcentage :", champPourcentage
        };

        int option = JOptionPane.showConfirmDialog(this, champs, "Modifier Réduction", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String nom = champNom.getText().trim();
                double pourcentage = Double.parseDouble(champPourcentage.getText().trim());

                if (nom.isEmpty() || pourcentage <= 0 || pourcentage >= 100) {
                    JOptionPane.showMessageDialog(this, "Nom ou pourcentage invalide.");
                    return;
                }

                r.setNom(nom);
                r.setPourcentage(pourcentage);

                boolean ok = new ReductionDAO().modifierReduction(r);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Réduction modifiée !");
                    rafraichirListe();
                } else {
                    JOptionPane.showMessageDialog(this, "Erreur modification.");
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
                JOptionPane.showMessageDialog(this, "Erreur suppression.");
            }
        }
    }
}
