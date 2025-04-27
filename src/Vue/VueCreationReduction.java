package Vue;

import DAO.ReductionDAO;
import modele.Reduction;

import javax.swing.*;
import java.awt.*;

public class VueCreationReduction extends JDialog { // ✅ JDialog (modale) au lieu de JFrame

    private final JTextField champPourcentage;
    private final JButton boutonAjouter;
    private final JButton boutonAnnuler;
    private final MainWindow mainWindow;

    public VueCreationReduction(MainWindow mainWindow) {
        super(mainWindow, "➕ Ajouter une Réduction", true); // ✅ Modal, attachée au MainWindow
        this.mainWindow = mainWindow;

        setSize(400, 250);
        setLocationRelativeTo(mainWindow);
        setLayout(new BorderLayout());

        StyleUI.appliquerFondEtCadre((JComponent) getContentPane());

        // --- Centre ---
        JPanel centre = new JPanel(new GridLayout(2, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        StyleUI.appliquerFondEtCadre(centre);

        centre.add(new JLabel("Pourcentage (%) :"));
        champPourcentage = new JTextField();
        centre.add(champPourcentage);

        add(centre, BorderLayout.CENTER);

        // --- Bas ---
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        StyleUI.appliquerFondEtCadre(bas);

        boutonAjouter = new JButton("💾 Ajouter");
        boutonAnnuler = new JButton("❌ Annuler");
        StyleUI.styliserBouton(boutonAjouter);
        StyleUI.styliserBouton(boutonAnnuler);

        bas.add(boutonAjouter);
        bas.add(boutonAnnuler);

        add(bas, BorderLayout.SOUTH);

        // --- Actions ---
        boutonAjouter.addActionListener(e -> ajouterReduction());
        boutonAnnuler.addActionListener(e -> dispose());
    }

    private void ajouterReduction() {
        try {
            double pourcentage = Double.parseDouble(champPourcentage.getText().trim());
            if (pourcentage <= 0 || pourcentage > 100) {
                JOptionPane.showMessageDialog(this, "Le pourcentage doit être entre 1 et 99.");
                return;
            }

            Reduction nouvelleReduction = new Reduction(pourcentage);
            boolean succes = new ReductionDAO().ajouterReduction(nouvelleReduction);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Réduction ajoutée avec succès !");
                dispose();
                if (mainWindow.getVues().containsKey("reductions")) {
                    VueGestionReductions vgr = (VueGestionReductions) mainWindow.getVues().get("reductions");
                    vgr.rafraichirListe();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de la réduction.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre valide.");
        }
    }
}
