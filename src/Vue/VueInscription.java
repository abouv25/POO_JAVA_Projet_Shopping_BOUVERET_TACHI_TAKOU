package Vue;

import DAO.QuestionSecuriteDAO;
import DAO.UtilisateurDAO;
import modele.QuestionSecurite;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VueInscription extends JPanel {

    private MainWindow mainWindow;

    private JTextField champNom, champPrenom, champEmail;
    private JPasswordField champMotDePasse, champConfirmation;
    private JCheckBox checkBoxFidele, checkBoxAdmin;
    private JButton boutonInscription, boutonAccueil, boutonRetour;

    private JComboBox<String> comboQ1, comboQ2, comboQ3;
    private JTextField champR1, champR2, champR3;

    public VueInscription(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        JPanel centre = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        centre.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        StyleUI.appliquerFondEtCadre(centre);

        JLabel titre = new JLabel("Créer un compte");
        StyleUI.styliserTitre(titre);

        champNom = new JTextField(20);
        champPrenom = new JTextField(20);
        champEmail = new JTextField(20);
        champMotDePasse = new JPasswordField(20);
        champConfirmation = new JPasswordField(20);
        checkBoxFidele = new JCheckBox("Client fidèle");
        checkBoxAdmin = new JCheckBox("Compte administrateur");

        List<String> questions = Arrays.asList(
                "Nom de votre premier animal ?",
                "Ville de naissance ?",
                "Nom de jeune fille de votre mère ?",
                "Votre plat préféré ?",
                "Votre professeur préféré ?"
        );
        comboQ1 = new JComboBox<>(questions.toArray(new String[0]));
        comboQ2 = new JComboBox<>(questions.toArray(new String[0]));
        comboQ3 = new JComboBox<>(questions.toArray(new String[0]));
        champR1 = new JTextField(20);
        champR2 = new JTextField(20);
        champR3 = new JTextField(20);

        boutonInscription = new JButton("Créer mon compte");
        boutonAccueil = new JButton("🏠 Accueil");
        boutonRetour = new JButton("⬅ Retour");
        StyleUI.styliserBouton(boutonInscription);
        StyleUI.styliserBouton(boutonAccueil);
        StyleUI.styliserBouton(boutonRetour);

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centre.add(titre, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Nom :"), gbc);
        gbc.gridx = 1;
        centre.add(champNom, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Prénom :"), gbc);
        gbc.gridx = 1;
        centre.add(champPrenom, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Email :"), gbc);
        gbc.gridx = 1;
        centre.add(champEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Mot de passe :"), gbc);
        gbc.gridx = 1;
        centre.add(champMotDePasse, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Confirmation MDP :"), gbc);
        gbc.gridx = 1;
        centre.add(champConfirmation, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(checkBoxFidele, gbc);
        gbc.gridx = 1;
        centre.add(checkBoxAdmin, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Question 1 :"), gbc);
        gbc.gridx = 1;
        centre.add(comboQ1, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Réponse 1 :"), gbc);
        gbc.gridx = 1;
        centre.add(champR1, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Question 2 :"), gbc);
        gbc.gridx = 1;
        centre.add(comboQ2, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Réponse 2 :"), gbc);
        gbc.gridx = 1;
        centre.add(champR2, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Question 3 :"), gbc);
        gbc.gridx = 1;
        centre.add(comboQ3, gbc);

        gbc.gridx = 0; gbc.gridy++;
        centre.add(new JLabel("Réponse 3 :"), gbc);
        gbc.gridx = 1;
        centre.add(champR3, gbc);

        gbc.gridx = 0; gbc.gridy++;
        gbc.gridwidth = 2;
        centre.add(boutonInscription, gbc);

        add(centre, BorderLayout.CENTER);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bas.add(boutonRetour);
        bas.add(boutonAccueil);
        add(bas, BorderLayout.SOUTH);

        boutonInscription.addActionListener(e -> creerCompte());
        boutonAccueil.addActionListener(e -> mainWindow.switchTo("accueil"));
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());
    }

    private void creerCompte() {
        String nom = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email = champEmail.getText().trim();
        String mdp = new String(champMotDePasse.getPassword());
        String confirmation = new String(champConfirmation.getPassword());
        boolean fidele = checkBoxFidele.isSelected();
        boolean admin = checkBoxAdmin.isSelected();

        String q1 = comboQ1.getSelectedItem().toString();
        String q2 = comboQ2.getSelectedItem().toString();
        String q3 = comboQ3.getSelectedItem().toString();
        String r1 = champR1.getText().trim();
        String r2 = champR2.getText().trim();
        String r3 = champR3.getText().trim();

        // Validations
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty() || confirmation.isEmpty() ||
                r1.isEmpty() || r2.isEmpty() || r3.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs sont requis.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Adresse email invalide.");
            return;
        }
        if (mdp.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mot de passe trop court (min. 6 caractères).");
            return;
        }
        if (!mdp.equals(confirmation)) {
            JOptionPane.showMessageDialog(this, "Les mots de passe ne correspondent pas.");
            return;
        }

        UtilisateurDAO dao = new UtilisateurDAO();
        if (dao.emailExistant(email)) {
            JOptionPane.showMessageDialog(this, "Email déjà utilisé.");
            return;
        }

        Utilisateur utilisateur = new Utilisateur(0, nom, prenom, email, mdp, admin, fidele);
        if (dao.ajouterUtilisateur(utilisateur)) {
            Map<String, String> questionsReponses = new HashMap<>();
            questionsReponses.put("question1", q1);
            questionsReponses.put("reponse1", r1);
            questionsReponses.put("question2", q2);
            questionsReponses.put("reponse2", r2);
            questionsReponses.put("question3", q3);
            questionsReponses.put("reponse3", r3);

            QuestionSecuriteDAO daoQ = new QuestionSecuriteDAO();
            daoQ.enregistrerQuestions(utilisateur.getEmail(), questionsReponses);

            JOptionPane.showMessageDialog(this, "Compte créé avec succès !");
            mainWindow.switchTo("connexion");
        } else {
            JOptionPane.showMessageDialog(this, "Erreur lors de la création du compte.");
        }
    }
}
