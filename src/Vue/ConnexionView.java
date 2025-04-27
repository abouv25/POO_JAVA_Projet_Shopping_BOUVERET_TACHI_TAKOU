package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ConnexionView extends JPanel {

    private MainWindow mainWindow;

    public ConnexionView(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // Bandeau haut avec barre de navigation
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // Titre
        JLabel titre = new JLabel("Connectez-vous pour accéder à votre espace personnel", SwingConstants.CENTER);
        StyleUI.styliserTitre(titre);
        add(titre, BorderLayout.NORTH);

        // Centre de la page
        JPanel centre = new JPanel(new GridBagLayout());
        StyleUI.appliquerFondEtCadre(centre);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel emailLabel = new JLabel("Adresse Email :");
        JTextField emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Se connecter");
        JButton signupButton = new JButton("S'inscrire");
        JButton accueilButton = new JButton("🏠 Accueil");

        // Styliser les boutons
        StyleUI.styliserBouton(loginButton);
        StyleUI.styliserBouton(signupButton);
        StyleUI.styliserBouton(accueilButton);

        // Mise en page
        gbc.gridx = 0; gbc.gridy = 0;
        centre.add(emailLabel, gbc);
        gbc.gridx = 1;
        centre.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(passwordLabel, gbc);
        gbc.gridx = 1;
        centre.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        centre.add(loginButton, gbc);

        gbc.gridy++;
        centre.add(signupButton, gbc);

        gbc.gridy++;
        centre.add(accueilButton, gbc);

        add(centre, BorderLayout.CENTER);

        // Actions des boutons
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            UtilisateurDAO dao = new UtilisateurDAO();
            Utilisateur utilisateur = dao.verifierConnexion(email, password);

            if (utilisateur != null) {
                JOptionPane.showMessageDialog(mainWindow, "Connexion réussie !");
                mainWindow.setUtilisateurConnecte(utilisateur);
                mainWindow.chargerCatalogue();
                mainWindow.switchTo("catalogue");
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Identifiants incorrects. Veuillez réessayer.");
            }
        });

        signupButton.addActionListener(e -> mainWindow.switchTo("inscription"));
        accueilButton.addActionListener(e -> mainWindow.switchTo("accueil"));
    }
}
