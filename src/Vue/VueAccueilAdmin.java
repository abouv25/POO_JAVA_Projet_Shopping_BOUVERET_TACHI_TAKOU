package Vue;

import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueAccueilAdmin extends JPanel {

    private final MainWindow mainWindow;
    private final JLabel titreLabel;
    private final JButton boutonProduits;
    private final JButton boutonReductions;
    private final JButton boutonStats;
    private final JButton boutonDeconnexion;

    public VueAccueilAdmin(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this); // ✅ Fond blanc + cadre noir

        // --- Logo centré en haut ---
        ImageIcon logo = new ImageIcon(getClass().getResource("/Vue/logoBTTShopping.png")); // ✅ Bon chemin relatif
        Image img = logo.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // --- Centre avec boutons admin ---
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        StyleUI.appliquerFondEtCadre(centre);

        titreLabel = new JLabel("👨‍💼 Espace Administrateur", SwingConstants.CENTER);
        titreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titreLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        centre.add(titreLabel);
        centre.add(Box.createRigidArea(new Dimension(0, 20)));

        boutonProduits = new JButton("📦 Gérer les Produits");
        boutonReductions = new JButton("🏷️ Gérer les Réductions");
        boutonStats = new JButton("📊 Statistiques / Reporting");
        boutonDeconnexion = new JButton("🚪 Déconnexion");

        for (JButton btn : new JButton[]{boutonProduits, boutonReductions, boutonStats, boutonDeconnexion}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            StyleUI.styliserBouton(btn); // ✅ Uniformisation des boutons
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
            if (!mainWindow.getVues().containsKey("reductions")) {
                VueGestionReductions vue = new VueGestionReductions(mainWindow);
                mainWindow.ajouterVue("reductions", vue);
            }
            mainWindow.switchTo("reductions");
        });

        boutonStats.addActionListener(e -> {
            mainWindow.switchTo("statistiques");
        });

        boutonDeconnexion.addActionListener(e -> {
            mainWindow.setUtilisateurConnecte(null);
            JOptionPane.showMessageDialog(mainWindow, "Déconnexion réussie.");
            mainWindow.switchTo("accueil");
        });
    }
}
