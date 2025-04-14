package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnexionView extends JPanel {

    private MainWindow mainWindow;

    public ConnexionView(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        // Définition de la disposition
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Titre de la page de connexion
        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        // Champ pour l'email
        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField(20);

        // Champ pour le mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField(20);

        // Bouton de connexion
        JButton loginButton = new JButton("Se connecter");

        // Action du bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer les données saisies
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                // Vérification des informations de connexion
                if (isValidLogin(email, password)) {
                    // Connexion réussie
                    mainWindow.setUserAuthenticated(true);  // Authentifier l'utilisateur
                    JOptionPane.showMessageDialog(mainWindow, "Connexion réussie !");
                    mainWindow.switchTo("accueil");  // Passage à la vue d'accueil après la connexion
                } else {
                    // Connexion échouée
                    JOptionPane.showMessageDialog(mainWindow, "Identifiants incorrects. Essayez à nouveau.");
                }
            }
        });

        // Configuration du layout pour positionner les composants
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
    }

    // Méthode pour vérifier les informations de connexion (remplacez par une logique réelle)
    private boolean isValidLogin(String email, String password) {
        // Exemple de validation simple (vous pouvez remplacer cela par une vraie vérification)
        // Exemple : email = "user@example.com" et mot de passe = "password123"
        return email.equals("user@example.com") && password.equals("password123");
    }
}
