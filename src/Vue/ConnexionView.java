package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnexionView extends JPanel {

    private MainWindow mainWindow;

    public ConnexionView(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Se connecter");
        JButton signupButton = new JButton("S'inscrire");
        JButton accueilButton = new JButton("Accueil");

        // Action de connexion
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                UtilisateurDAO dao = new UtilisateurDAO();

                Utilisateur utilisateur = dao.verifierConnexion(email, password);

                if (utilisateur != null) {
                    JOptionPane.showMessageDialog(mainWindow, "Connexion réussie !");
                    mainWindow.setUtilisateurConnecte(utilisateur);

                    // Chargement dynamique de VueProduits
                    mainWindow.chargerCatalogue();
                    mainWindow.switchTo("catalogue");
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Identifiants incorrects.");
                }
            }
        });

        // Action d'inscription
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.switchTo("inscription");  // Passer à la page d'inscription
            }
        });

        // Action pour revenir à l'accueil
        accueilButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainWindow.switchTo("accueil");
            }
        });

        // Mise en page
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy++;
        add(signupButton, gbc);  // Ajout du bouton "S'inscrire"

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        add(accueilButton, gbc);  // Ajout du bouton "Accueil"
    }
}
