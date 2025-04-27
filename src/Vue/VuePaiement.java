package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Panier;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VuePaiement extends JPanel {

    private MainWindow mainWindow;
    private JTextField champNumeroCarte;
    private JTextField champNomCarte;
    private JTextField champAdresseLivraison;
    private JButton boutonPayer;
    private JButton boutonRetour;

    public VuePaiement(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Centre
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 80, 10, 80));
        StyleUI.appliquerFondEtCadre(centre);

        JLabel titre = new JLabel("💳 Paiement de la Commande", SwingConstants.CENTER);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        centre.add(titre);
        centre.add(Box.createRigidArea(new Dimension(0, 20)));

        champNumeroCarte = new JTextField();
        champNomCarte = new JTextField();
        champAdresseLivraison = new JTextField();

        ajouterChamp(centre, "Numéro de Carte Bancaire :", champNumeroCarte);
        ajouterChamp(centre, "Nom sur la Carte :", champNomCarte);
        ajouterChamp(centre, "Adresse de Livraison :", champAdresseLivraison);

        add(centre, BorderLayout.CENTER);

        // ✅ Bas
        JPanel bas = new JPanel(new FlowLayout());
        bas.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        StyleUI.appliquerFondEtCadre(bas);

        boutonPayer = new JButton("✅ Confirmer Paiement");
        boutonRetour = new JButton("⬅ Retour");

        StyleUI.styliserBouton(boutonPayer);
        StyleUI.styliserBouton(boutonRetour);

        bas.add(boutonRetour);
        bas.add(boutonPayer);

        add(bas, BorderLayout.SOUTH);

        // ✅ Listeners
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());
        boutonPayer.addActionListener(e -> validerPaiement());
    }

    private void ajouterChamp(JPanel panel, String labelTexte, JTextField champ) {
        JLabel label = new JLabel(labelTexte);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        champ.setMaximumSize(new Dimension(400, 30));
        panel.add(label);
        panel.add(champ);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void validerPaiement() {
        String carte = champNumeroCarte.getText().trim();
        String nom = champNomCarte.getText().trim();
        String adresse = champAdresseLivraison.getText().trim();

        if (carte.isEmpty() || nom.isEmpty() || adresse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Merci de remplir tous les champs !");
            return;
        }

        if (carte.length() < 12 || carte.length() > 19) {
            JOptionPane.showMessageDialog(this, "Numéro de carte invalide !");
            return;
        }

        Utilisateur u = mainWindow.getUtilisateurConnecte();
        Panier panier = mainWindow.getPanier();

        if (u == null || panier.estVide()) {
            JOptionPane.showMessageDialog(this, "Erreur : Utilisateur ou panier vide !");
            mainWindow.switchTo("accueil");
            return;
        }

        // ✅ Créer une facture
        Facture facture = new Facture(u, panier.getLignes());
        facture.calculerMontantTotal();

        // ✅ Enregistrer la facture en base
        FactureDAO dao = new FactureDAO();
        int idFacture = dao.ajouterFacture(facture);
        if (idFacture > 0) {
            facture.setId(idFacture);
        }

        // ✅ Vider le panier
        panier.viderPanier();

        // ✅ Message succès
        JOptionPane.showMessageDialog(this, "💳 Paiement validé et facture générée !");

        // ✅ Afficher VueDetailFacture (et plus VueNouvelleFacture)
        VueDetailFacture vueFacture = new VueDetailFacture(idFacture, u, mainWindow);
        mainWindow.ajouterVue("facture_" + idFacture, vueFacture);
        mainWindow.switchTo("facture_" + idFacture);
    }
}
