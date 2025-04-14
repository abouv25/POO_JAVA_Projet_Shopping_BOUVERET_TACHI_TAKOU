package Vue;

import javax.swing.*;
import java.awt.*;

public class ProduitsView extends JPanel {
    public ProduitsView(MainWindow mainWindow) {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("Liste des Produits", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        // Ajout d'un bouton pour naviguer vers la vue des factures
        JButton btnGoToFactures = new JButton("Voir les factures");
        btnGoToFactures.addActionListener(e -> mainWindow.switchTo("factures"));
        add(btnGoToFactures, BorderLayout.SOUTH);
    }
}
