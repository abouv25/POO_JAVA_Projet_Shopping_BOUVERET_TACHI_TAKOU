package Vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class VueLogin extends JPanel {

    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JButton boutonConnexion;
    private JButton boutonAccueil;
    private JButton boutonMotDePasseOublie;
    private MainWindow mainWindow;

    public VueLogin(MainWindow mainWindow) {
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        champEmail = new JTextField(20);
        champMotDePasse = new JPasswordField(20);
        boutonConnexion = new JButton("🔐 Se connecter");
        boutonAccueil = new JButton("🏠 Accueil");
        boutonMotDePasseOublie = new JButton("❓ Mot de passe oublié ?");

        JPanel centre = new JPanel(new GridLayout(4, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        StyleUI.appliquerFondEtCadre(centre);

        JLabel labelEmail = new JLabel("Email :");
        JLabel labelMdp = new JLabel("Mot de passe :");
        centre.add(labelEmail);
        centre.add(champEmail);
        centre.add(labelMdp);
        centre.add(champMotDePasse);
        centre.add(new JLabel()); // vide
        centre.add(boutonConnexion);
        centre.add(new JLabel()); // vide
        centre.add(boutonMotDePasseOublie);

        StyleUI.styliserBouton(boutonConnexion);
        StyleUI.styliserBouton(boutonMotDePasseOublie);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bas.add(boutonAccueil);

        add(centre, BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
        boutonMotDePasseOublie.addActionListener(e -> mainWindow.switchTo("motdepasseoublie"));
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
