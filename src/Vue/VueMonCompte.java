package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueMonCompte extends JPanel {

    private final MainWindow mainWindow;
    private final JTextField champPrenom;
    private final JTextField champNom;
    private final JTextField champEmail;
    private final JPasswordField champMotDePasse;
    private final JLabel labelFidelite;
    private final JLabel labelRole;

    public VueMonCompte(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // --- Barre supérieure ---
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // --- Centre ---
        JPanel centre = new JPanel(new GridLayout(6, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        StyleUI.appliquerFondEtCadre(centre);

        champPrenom = new JTextField();
        champNom = new JTextField();
        champEmail = new JTextField();
        champEmail.setEditable(false);
        champMotDePasse = new JPasswordField();

        labelFidelite = new JLabel();
        labelRole = new JLabel();

        centre.add(new JLabel("Prénom :"));
        centre.add(champPrenom);
        centre.add(new JLabel("Nom :"));
        centre.add(champNom);
        centre.add(new JLabel("Email :"));
        centre.add(champEmail);
        centre.add(new JLabel("Mot de passe :"));
        centre.add(champMotDePasse);
        centre.add(new JLabel("Client fidèle :"));
        centre.add(labelFidelite);
        centre.add(new JLabel("Rôle :"));
        centre.add(labelRole);

        add(centre, BorderLayout.CENTER);

        // --- Bas ---
        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        StyleUI.appliquerFondEtCadre(bas);

        JButton boutonEnregistrer = new JButton("💾 Enregistrer Modifications");
        JButton boutonRetour = new JButton("⬅ Retour Accueil");

        StyleUI.styliserBouton(boutonEnregistrer);
        StyleUI.styliserBouton(boutonRetour);

        bas.add(boutonEnregistrer);
        bas.add(boutonRetour);

        add(bas, BorderLayout.SOUTH);

        // --- Listeners ---
        boutonEnregistrer.addActionListener(e -> enregistrerModifications());
        boutonRetour.addActionListener(e -> mainWindow.switchTo("accueil"));

        chargerInfosUtilisateur();
    }

    private void chargerInfosUtilisateur() {
        Utilisateur u = mainWindow.getUtilisateurConnecte();
        if (u != null) {
            champPrenom.setText(u.getPrenom());
            champNom.setText(u.getNom());
            champEmail.setText(u.getEmail());
            champMotDePasse.setText(u.getMotDePasse());
            labelFidelite.setText(u.isClientFidele() ? "✅ Oui" : "❌ Non");
            labelRole.setText(u.isEstAdmin() ? "👑 Administrateur" : "👤 Utilisateur");
        }
    }

    private void enregistrerModifications() {
        try {
            Utilisateur u = mainWindow.getUtilisateurConnecte();
            if (u == null) return;

            u.setPrenom(champPrenom.getText().trim());
            u.setNom(champNom.getText().trim());
            u.setMotDePasse(new String(champMotDePasse.getPassword()).trim());

            boolean succes = new UtilisateurDAO().modifierUtilisateur(u);

            if (succes) {
                JOptionPane.showMessageDialog(this, "Modifications enregistrées !");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
        }
    }
}
