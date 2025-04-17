package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueInscription extends JPanel {

    private MainWindow mainWindow;

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JCheckBox checkBoxFidele;
    private JCheckBox checkBoxAdmin;
    private JButton boutonInscription;
    private JButton boutonAccueil;

    public VueInscription(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());

        JLabel titre = new JLabel("Créer un compte", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 20));
        add(titre, BorderLayout.NORTH);

        // Formulaire
        JPanel centre = new JPanel(new GridLayout(7, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        champNom = new JTextField();
        champPrenom = new JTextField();
        champEmail = new JTextField();
        champMotDePasse = new JPasswordField();
        checkBoxFidele = new JCheckBox("Client fidèle");
        checkBoxAdmin = new JCheckBox("Compte administrateur");

        centre.add(new JLabel("Nom :"));
        centre.add(champNom);
        centre.add(new JLabel("Prénom :"));
        centre.add(champPrenom);
        centre.add(new JLabel("Email :"));
        centre.add(champEmail);
        centre.add(new JLabel("Mot de passe :"));
        centre.add(champMotDePasse);
        centre.add(new JLabel(""));
        centre.add(checkBoxFidele);
        centre.add(new JLabel(""));
        centre.add(checkBoxAdmin);

        boutonInscription = new JButton("Créer mon compte");
        centre.add(new JLabel(""));
        centre.add(boutonInscription);

        add(centre, BorderLayout.CENTER);

        // Bas : bouton Accueil
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boutonAccueil = new JButton("Accueil");
        bas.add(boutonAccueil);
        add(bas, BorderLayout.SOUTH);

        // Actions
        boutonInscription.addActionListener(e -> creerCompte());
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
    }

    private void creerCompte() {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword());
        boolean fidele = checkBoxFidele.isSelected();
        boolean admin = checkBoxAdmin.isSelected();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.");
            return;
        }

        Utilisateur utilisateur = new Utilisateur(0, nom, prenom, email, motDePasse, admin, fidele);
        UtilisateurDAO dao = new UtilisateurDAO();

        if (dao.ajouterUtilisateur(utilisateur)) {
            JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
            mainWindow.switchTo("connexion");
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte.");
        }
    }
}
