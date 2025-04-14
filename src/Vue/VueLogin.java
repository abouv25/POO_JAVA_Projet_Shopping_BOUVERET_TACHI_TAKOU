package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Interface graphique de connexion utilisateur.
 */
public class VueLogin extends JFrame {

    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;

    public VueLogin() {
        setTitle("Connexion - Projet Shopping");
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centre la fenêtre

        champEmail = new JTextField(20);
        champMotDePasse = new JPasswordField(20);
        boutonConnexion = new JButton("Se connecter");

        // Mise en page
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Email :"));
        panel.add(champEmail);

        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMotDePasse);

        panel.add(new JLabel()); // vide
        panel.add(boutonConnexion);

        add(panel);
    }

    public String getEmail() {
        return champEmail.getText();
    }

    public String getMotDePasse() {
        return new String(champMotDePasse.getPassword());
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void ajouterListenerConnexion(ActionListener listener) {
        boutonConnexion.addActionListener(listener);
    }
}
