package App;

import modele.Utilisateur;
import Vue.VueDetailFacture;

import javax.swing.*;

public class MainExportPDFTest {
    public static void main(String[] args) {
        int idFacture = 1;

        // 🔹 Utilisateur fictif pour le test
        Utilisateur utilisateur = new Utilisateur(1, "Antoine", "antoine@mail.com", "mdp123", true);

        SwingUtilities.invokeLater(() ->
                new VueDetailFacture(idFacture, utilisateur).setVisible(true));
    }
}
