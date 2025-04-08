package App;

import controleur.ControleConnexion;
import vue.VueLogin;

import javax.swing.*;

public class MainLoginTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VueLogin fenetre = new VueLogin();
            new ControleConnexion(fenetre); // 👈 on connecte la vue au contrôleur
            fenetre.setVisible(true);
        });
    }
}
