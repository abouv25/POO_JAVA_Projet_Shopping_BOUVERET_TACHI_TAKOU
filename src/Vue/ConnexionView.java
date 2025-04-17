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

        setLayout(new BorderLayout());

        // --- Partie centrale avec GridBagLayout pour le formulaire ---
        JPanel centre = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Se connecter");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                UtilisateurDAO dao = new UtilisateurDAO();
                Utilisateur utilisateur = dao.trouverParEmailEtMotDePasse(email, password);

                if (utilisateur != null) {
                    JOptionPane.showMessageDialog(mainWindow, "Connexion réussie !");
                    mainWindow.setUtilisateurConnecte(utilisateur);

                    // ✅ Ajout dynamique de VueProduits
                    VueProduits vueProduits = new VueProduits(mainWindow, utilisateur);
                    mainWindow.ajouterVue("catalogue", vueProduits);

                    mainWindow.switchTo("catalogue");
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Identifiants incorrects.");
                }
            }
        });

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centre.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        centre.add(emailLabel, gbc);
        gbc.gridx = 1;
        centre.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(passwordLabel, gbc);
        gbc.gridx = 1;
        centre.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        centre.add(loginButton, gbc);

        add(centre, BorderLayout.CENTER);

        // --- Bas : bouton accueil ---
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton boutonAccueil = new JButton("Accueil");
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
        bas.add(boutonAccueil);

        add(bas, BorderLayout.SOUTH);
    }
}
