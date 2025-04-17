package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Interface graphique de connexion utilisateur.
 */
public class VueLogin extends JPanel {

    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private JButton boutonAccueil;
    private MainWindow mainWindow;

    public VueLogin(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        champEmail = new JTextField(20);
        champMotDePasse = new JPasswordField(20);
        boutonConnexion = new JButton("Se connecter");
        boutonAccueil = new JButton("Accueil");

        // Formulaire
        JPanel centre = new JPanel(new GridLayout(3, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        centre.add(new JLabel("Email :"));
        centre.add(champEmail);
        centre.add(new JLabel("Mot de passe :"));
        centre.add(champMotDePasse);
        centre.add(new JLabel()); // vide
        centre.add(boutonConnexion);

        // Bas de l’écran avec bouton Accueil
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bas.add(boutonAccueil);

        // Ajout au panneau principal
        setLayout(new BorderLayout());
        add(centre, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        // Action bouton accueil
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
    }

    public String getEmail() {
        return champEmail.getText();
    }

    public String getMotDePasse() {
        return new String(champMotDePasse.getPassword());
    }

    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(mainWindow, message);
    }

    public void ajouterListenerConnexion(ActionListener listener) {
        boutonConnexion.addActionListener(listener);
    }
}
