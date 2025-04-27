package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class VueAjoutUtilisateur extends JPanel {

    private final MainWindow mainWindow;
    private final JTextField champNom;
    private final JTextField champPrenom;
    private final JTextField champEmail;
    private final JPasswordField champMotDePasse;
    private final JComboBox<String> roleComboBox;
    private final JButton boutonAjouter;
    private final JButton boutonAnnuler;

    public VueAjoutUtilisateur(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Centre : formulaire
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        StyleUI.appliquerFondEtCadre(form);

        champNom = new JTextField();
        champPrenom = new JTextField();
        champEmail = new JTextField();
        champMotDePasse = new JPasswordField();
        roleComboBox = new JComboBox<>(new String[]{"Client", "Admin"});

        form.add(new JLabel("Nom :"));
        form.add(champNom);
        form.add(new JLabel("Prénom :"));
        form.add(champPrenom);
        form.add(new JLabel("Email :"));
        form.add(champEmail);
        form.add(new JLabel("Mot de passe :"));
        form.add(champMotDePasse);
        form.add(new JLabel("Rôle :"));
        form.add(roleComboBox);

        add(form, BorderLayout.CENTER);

        // ✅ Bas : boutons
        JPanel bas = new JPanel(new FlowLayout());
        StyleUI.appliquerFondEtCadre(bas);

        boutonAjouter = new JButton("➕ Ajouter");
        boutonAnnuler = new JButton("⬅ Retour");

        StyleUI.styliserBouton(boutonAjouter);
        StyleUI.styliserBouton(boutonAnnuler);

        bas.add(boutonAnnuler);
        bas.add(boutonAjouter);

        add(bas, BorderLayout.SOUTH);

        // ✅ Listeners
        boutonAnnuler.addActionListener(e -> mainWindow.retourPagePrecedente());
        boutonAjouter.addActionListener(e -> ajouterUtilisateur());
    }

    private void ajouterUtilisateur() {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String motDePasse = new String(champMotDePasse.getPassword()).trim();
        boolean estAdmin = roleComboBox.getSelectedItem().equals("Admin");

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Merci de remplir tous les champs !");
            return;
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(prenom);
        utilisateur.setEmail(email);
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setEstAdmin(estAdmin);

        UtilisateurDAO dao = new UtilisateurDAO();
        boolean success = dao.ajouterUtilisateur(utilisateur);

        if (success) {
            JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !");
            mainWindow.switchTo("gestionUtilisateurs"); // ➔ Retour à la gestion
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur.");
        }
    }
}
