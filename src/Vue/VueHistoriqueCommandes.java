package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueHistoriqueCommandes extends JPanel {

    public VueHistoriqueCommandes(MainWindow mainWindow) {
        setLayout(new BorderLayout());

        JLabel titre = new JLabel("📜 Historique de vos commandes", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        add(titre, BorderLayout.NORTH);

        // Placeholder tableau de commandes
        String[] colonnes = {"Date", "N° Facture", "Total", "Détails"};
        Object[][] donnees = {}; // à remplir plus tard depuis la base

        JTable table = new JTable(donnees, colonnes);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton retour = new JButton("⬅ Retour accueil");
        retour.addActionListener(e -> mainWindow.switchTo("accueilClient"));
        JPanel bas = new JPanel();
        bas.add(retour);
        add(bas, BorderLayout.SOUTH);
    }
}
