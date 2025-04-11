package Vue;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainWindow() {
        setTitle("Application Shopping");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Ajout des vues (tu en rajouteras ici)
        ConnexionView connexionView = new ConnexionView(this);
        mainPanel.add(connexionView, "connexion");

        // Ajout du panel principal
        setContentPane(mainPanel);

        // Affiche l’écran de connexion au lancement
        cardLayout.show(mainPanel, "connexion");

        setVisible(true);
    }

    // Méthode pour changer de vue
    public void switchTo(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    // Getter pour le panel principal si besoin
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
