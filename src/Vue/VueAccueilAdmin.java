package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueAccueilAdmin extends JPanel {

    private MainWindow mainWindow;
    private JLabel titreLabel;
    private JButton boutonProduits;
    private JButton boutonReductions;
    private JButton boutonStats;
    private JButton boutonDeconnexion;

    public VueAccueilAdmin(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());

        // --- Logo centré en haut ---
        ImageIcon logo = new ImageIcon(getClass().getResource("logoBTTShopping.png"));
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // --- Centre avec boutons admin ---
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        titreLabel = new JLabel("Espace Administrateur", SwingConstants.CENTER);
        titreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        centre.add(titreLabel);
        centre.add(Box.createRigidArea(new Dimension(0, 20)));

        boutonProduits = new JButton("📦 Gérer les Produits");
        boutonReductions = new JButton("🏷️ Gérer les Réductions");
        boutonStats = new JButton("📊 Statistiques / Reporting");
        boutonDeconnexion = new JButton("🚪 Déconnexion");

        for (JButton btn : new JButton[]{boutonProduits, boutonReductions, boutonStats, boutonDeconnexion}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            centre.add(btn);
            centre.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(centre, BorderLayout.CENTER);

        // --- Listeners ---
        boutonProduits.addActionListener(e -> {
            mainWindow.chargerVueAdmin();
            mainWindow.switchTo("admin");
        });

        boutonReductions.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainWindow, "La gestion des réductions arrive bientôt !");
        });

        boutonStats.addActionListener(e -> {
            mainWindow.switchTo("stats"); // La vue sera créée ensuite
        });

        boutonDeconnexion.addActionListener(e -> {
            mainWindow.setUtilisateurConnecte(null);
            JOptionPane.showMessageDialog(mainWindow, "Déconnexion réussie.");
            mainWindow.switchTo("accueil");
        });
    }
}
