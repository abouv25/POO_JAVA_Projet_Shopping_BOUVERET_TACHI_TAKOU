package Vue;

import DAO.ProduitDAO;
import modele.Produit;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VueAccueil extends JPanel {

    private final MainWindow mainWindow;
    private final JLabel bienvenueLabel;
    private final JButton boutonCatalogue;
    private final JButton boutonConnexionOuInfos;
    private final JButton boutonPanier;
    private final JButton boutonHistorique;
    private final JButton boutonDeconnexion;
    private final JButton boutonAdminProduits;
    private final JButton boutonAdminReductions;
    private final JButton boutonAdminStats;
    private final JPanel carrouselPanel;
    private final Timer carrouselTimer;
    private int carrouselIndex = 0;
    private List<Produit> produitsCarrousel;

    public VueAccueil(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // --- Logo ---
        ImageIcon logo = new ImageIcon(getClass().getResource("/Vue/logoBTTShopping.png"));
        Image imgReduite = logo.getImage().getScaledInstance(
                logo.getIconWidth() / 4,
                logo.getIconHeight() / 4,
                Image.SCALE_SMOOTH
        );
        JLabel logoLabel = new JLabel(new ImageIcon(imgReduite));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(logoLabel, BorderLayout.NORTH);

        // --- Centre (boutons + message) ---
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 80, 10, 80));
        StyleUI.appliquerFondEtCadre(centre);

        bienvenueLabel = new JLabel("", SwingConstants.CENTER);
        bienvenueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        StyleUI.styliserTitre(bienvenueLabel);
        centre.add(bienvenueLabel);
        centre.add(Box.createRigidArea(new Dimension(0, 20)));

        boutonCatalogue = new JButton("🛍️ Catalogue Produits");
        boutonConnexionOuInfos = new JButton(); // texte mis à jour dynamiquement
        boutonPanier = new JButton("🛒 Mon Panier");
        boutonHistorique = new JButton("📄 Mes Factures");
        boutonDeconnexion = new JButton("🚪 Déconnexion");

        boutonAdminProduits = new JButton("📦 Gérer Produits");
        boutonAdminReductions = new JButton("🏷️ Gérer Réductions");
        boutonAdminStats = new JButton("📊 Voir Statistiques");

        for (JButton btn : new JButton[]{
                boutonCatalogue, boutonConnexionOuInfos, boutonPanier, boutonHistorique,
                boutonAdminProduits, boutonAdminReductions, boutonAdminStats, boutonDeconnexion
        }) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(250, 40));
            StyleUI.styliserBouton(btn);
            centre.add(btn);
            centre.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        add(centre, BorderLayout.CENTER);

        // --- Bas (carrousel) ---
        JPanel bas = new JPanel(new BorderLayout());
        StyleUI.appliquerFondEtCadre(bas);

        JButton flecheGauche = new JButton("◀");
        JButton flecheDroite = new JButton("▶");
        StyleUI.styliserBouton(flecheGauche);
        StyleUI.styliserBouton(flecheDroite);

        flecheGauche.addActionListener(e -> faireDefilerCarrousel(-1));
        flecheDroite.addActionListener(e -> faireDefilerCarrousel(1));

        carrouselPanel = new JPanel();
        carrouselPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        StyleUI.appliquerFondEtCadre(carrouselPanel);

        bas.add(flecheGauche, BorderLayout.WEST);
        bas.add(carrouselPanel, BorderLayout.CENTER);
        bas.add(flecheDroite, BorderLayout.EAST);

        add(bas, BorderLayout.SOUTH);

        // --- Listeners principaux ---
        boutonCatalogue.addActionListener(e -> mainWindow.switchTo("catalogue"));

        boutonConnexionOuInfos.addActionListener(e -> {
            Utilisateur u = mainWindow.getUtilisateurConnecte();
            if (u != null) {
                mainWindow.switchTo("moncompte"); // la vue "moncompte" sera créée juste après
            } else {
                mainWindow.switchTo("connexion");
            }
        });

        boutonPanier.addActionListener(e -> {
            if (mainWindow.getUtilisateurConnecte() != null) {
                mainWindow.switchTo("panier");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez vous connecter pour accéder au panier.");
                mainWindow.switchTo("connexion");
            }
        });

        boutonHistorique.addActionListener(e -> {
            Utilisateur u = mainWindow.getUtilisateurConnecte();
            if (u != null) {
                mainWindow.switchTo("historique");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez vous connecter.");
            }
        });

        boutonDeconnexion.addActionListener(e -> {
            mainWindow.setUtilisateurConnecte(null);
            JOptionPane.showMessageDialog(mainWindow, "Déconnexion réussie !");
            mainWindow.switchTo("accueil");
        });

        boutonAdminProduits.addActionListener(e -> {
            mainWindow.chargerVueAdmin();
            mainWindow.switchTo("admin");
        });

        boutonAdminReductions.addActionListener(e -> {
            mainWindow.chargerVueReductions();
            mainWindow.switchTo("reductions");
        });

        boutonAdminStats.addActionListener(e -> {
            mainWindow.switchTo("statistiques");
        });

        // --- Carrousel produits ---
        produitsCarrousel = new ProduitDAO().getProduitsAleatoires(15);
        rafraichirCarrousel();

        carrouselTimer = new Timer(3000, e -> faireDefilerCarrousel(1));
        carrouselTimer.start();

        mettreAJourAffichage();
    }

    private void faireDefilerCarrousel(int direction) {
        carrouselIndex = (carrouselIndex + direction + produitsCarrousel.size()) % produitsCarrousel.size();
        rafraichirCarrousel();
    }

    private void rafraichirCarrousel() {
        carrouselPanel.removeAll();
        for (int i = 0; i < 5; i++) {
            int index = (carrouselIndex + i) % produitsCarrousel.size();
            Produit p = produitsCarrousel.get(index);
            JPanel carte = ComposantsUI.creerCarteProduit(p, mainWindow);

            carte.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            carte.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    VueDetailProduit vue = new VueDetailProduit(mainWindow, p, true);
                    vue.setVisible(true);
                }
                public void mouseEntered(MouseEvent e) {
                    carte.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
                public void mouseExited(MouseEvent e) {
                    carte.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }
            });

            carrouselPanel.add(carte);
        }
        carrouselPanel.revalidate();
        carrouselPanel.repaint();
    }

    public void mettreAJourAffichage() {
        Utilisateur u = mainWindow.getUtilisateurConnecte();
        boolean connecte = (u != null);
        boolean admin = connecte && u.isEstAdmin();

        bienvenueLabel.setText(connecte
                ? "Bonjour " + u.getPrenom() + " " + u.getNom() + " !"
                : "Qu’est-ce qui te ferait plaisir aujourd’hui ?");

        boutonConnexionOuInfos.setText(connecte ? "👤 Mon Compte" : "🔑 Connexion");
        boutonConnexionOuInfos.setVisible(true);
        boutonHistorique.setVisible(connecte);
        boutonPanier.setVisible(true);
        boutonDeconnexion.setVisible(connecte);

        // Admin uniquement
        boutonAdminProduits.setVisible(admin);
        boutonAdminReductions.setVisible(admin);
        boutonAdminStats.setVisible(admin);
    }
}
