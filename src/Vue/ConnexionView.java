package Vue;

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

        // Simule une connexion et bascule vers le catalogue
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // À remplacer par vérification réelle via UtilisateurDAO
                JOptionPane.showMessageDialog(mainWindow, "Connexion réussie !");
                mainWindow.switchTo("catalogue"); // prochaine vue à créer
            }
        });

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
}
