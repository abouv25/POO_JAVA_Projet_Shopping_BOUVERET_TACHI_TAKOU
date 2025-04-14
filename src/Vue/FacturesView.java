package Vue;

import javax.swing.*;
import java.awt.*;

public class FacturesView extends JPanel {
    public FacturesView(MainWindow mainWindow) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Historique des Factures", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Ajout d'un bouton pour revenir à l'écran d'accueil
        JButton btnGoToAccueil = new JButton("Retour à l'accueil");
        btnGoToAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
        add(btnGoToAccueil, BorderLayout.SOUTH);
    }
}
