package Vue;

import javax.swing.*;
import java.awt.*;

public class StyleUI {

    public static void appliquerFondEtCadre(JComponent comp) {
        comp.setBackground(Color.WHITE);
        comp.setForeground(Color.BLACK);
        if (comp instanceof JPanel) {
            ((JPanel) comp).setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }
    public static void appliquerStyleTableau(JTable table) {
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(Color.LIGHT_GRAY);
        table.setSelectionBackground(new Color(220, 220, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(26);
    }


    public static void styliserBouton(JButton btn) {
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public static void styliserTitre(JLabel label) {
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // ✅ Méthode manquante pour corriger ton erreur
    public static void styliserTexte(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setForeground(Color.BLACK);
    }

    public static void appliquerStyleComplet(JComponent comp) {
        appliquerFondEtCadre(comp);
        for (Component c : comp.getComponents()) {
            if (c instanceof JComponent) {
                appliquerFondEtCadre((JComponent) c);
            }
        }
    }
}
