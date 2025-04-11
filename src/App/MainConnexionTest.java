package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainConnexionTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Test Connexion");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            // ConnexionView intégrée dans ce fichier
            JPanel connexionView = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            JLabel title = new JLabel("Connexion");
            title.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel emailLabel = new JLabel("Email :");
            JTextField emailField = new JTextField(20);

            JLabel passwordLabel = new JLabel("Mot de passe :");
            JPasswordField passwordField = new JPasswordField(20);

            JButton loginButton = new JButton("Se connecter");

            loginButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame, "Connexion réussie !");
                }
            });

            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            connexionView.add(title, gbc);

            gbc.gridwidth = 1;
            gbc.gridy++;
            connexionView.add(emailLabel, gbc);
            gbc.gridx = 1;
            connexionView.add(emailField, gbc);

            gbc.gridx = 0; gbc.gridy++;
            connexionView.add(passwordLabel, gbc);
            gbc.gridx = 1;
            connexionView.add(passwordField, gbc);

            gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
            connexionView.add(loginButton, gbc);

            mainPanel.add(connexionView, "connexion");

            frame.setContentPane(mainPanel);
            cardLayout.show(mainPanel, "connexion");

            frame.setVisible(true);
        });
    }
}
