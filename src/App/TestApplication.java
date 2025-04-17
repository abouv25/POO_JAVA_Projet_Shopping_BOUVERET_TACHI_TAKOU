package App;

import Vue.MainWindow;

import javax.swing.*;

public class TestApplication {
    public static void main(String[] args) {
        // Lancement de l'application dans le thread graphique
        SwingUtilities.invokeLater(() -> {
            new MainWindow();
        });
    }
}
