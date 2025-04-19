package Vue;

import DAO.QuestionSecuriteDAO;
import DAO.UtilisateurDAO;

import javax.swing.*;
import java.awt.*;

public class VueMotDePasseOublie extends JPanel {

    private MainWindow mainWindow;
    private JTextField champEmail;
    private JComboBox<String> question1, question2, question3;
    private JTextField reponse1, reponse2, reponse3;
    private JPasswordField nouveauMotDePasse;
    private JButton validerBouton, retourBouton;

    public VueMotDePasseOublie(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        JLabel titre = new JLabel("🔒 Réinitialisation du mot de passe", SwingConstants.CENTER);
        StyleUI.styliserTitre(titre);
        add(titre, BorderLayout.NORTH);

        JPanel centre = new JPanel(new GridLayout(8, 2, 10, 10));
        centre.setBorder(BorderFactory.createEmptyBorder(20, 60, 20, 60));
        StyleUI.appliquerFondEtCadre(centre);

        champEmail = new JTextField();
        String[] questions = {
                "Quel est le nom de votre premier animal ?",
                "Quel est votre plat préféré ?",
                "Quel est le prénom de votre mère ?",
                "Quel est le nom de votre école primaire ?",
                "Quel est votre film préféré ?",
                "Quel est le nom de votre meilleur ami d'enfance ?",
                "Quelle est votre ville de naissance ?",
                "Quel est votre livre préféré ?",
                "Quel est votre sport préféré ?",
                "Quel est votre surnom d'enfance ?"
        };

        question1 = new JComboBox<>(questions);
        reponse1 = new JTextField();
        question2 = new JComboBox<>(questions);
        reponse2 = new JTextField();
        question3 = new JComboBox<>(questions);
        reponse3 = new JTextField();
        nouveauMotDePasse = new JPasswordField();

        centre.add(new JLabel("Email associé au compte :")); centre.add(champEmail);
        centre.add(new JLabel("Question 1 :")); centre.add(question1);
        centre.add(new JLabel("Réponse 1 :")); centre.add(reponse1);
        centre.add(new JLabel("Question 2 :")); centre.add(question2);
        centre.add(new JLabel("Réponse 2 :")); centre.add(reponse2);
        centre.add(new JLabel("Question 3 :")); centre.add(question3);
        centre.add(new JLabel("Réponse 3 :")); centre.add(reponse3);
        centre.add(new JLabel("Nouveau mot de passe :")); centre.add(nouveauMotDePasse);

        add(centre, BorderLayout.CENTER);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        validerBouton = new JButton("✅ Valider");
        retourBouton = new JButton("⬅️ Retour");
        bas.add(retourBouton);
        bas.add(validerBouton);
        add(bas, BorderLayout.SOUTH);

        retourBouton.addActionListener(e -> mainWindow.switchTo("connexion"));

        validerBouton.addActionListener(e -> {
            String email = champEmail.getText().trim();
            String q1 = (String) question1.getSelectedItem();
            String r1 = reponse1.getText().trim();
            String q2 = (String) question2.getSelectedItem();
            String r2 = reponse2.getText().trim();
            String q3 = (String) question3.getSelectedItem();
            String r3 = reponse3.getText().trim();
            String nouveauMDP = new String(nouveauMotDePasse.getPassword());

            if (email.isEmpty() || nouveauMDP.isEmpty() || r1.isEmpty() || r2.isEmpty() || r3.isEmpty()) {
                JOptionPane.showMessageDialog(mainWindow, "Veuillez remplir tous les champs.");
                return;
            }

            QuestionSecuriteDAO dao = new QuestionSecuriteDAO();
            boolean verifie = dao.verifierQuestions(email, q1, r1, q2, r2, q3, r3);

            if (verifie) {
                UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
                boolean modif = utilisateurDAO.modifierMotDePasse(email, nouveauMDP);
                if (modif) {
                    JOptionPane.showMessageDialog(mainWindow, "Mot de passe modifié avec succès !");
                    mainWindow.switchTo("connexion");
                } else {
                    JOptionPane.showMessageDialog(mainWindow, "Erreur lors de la mise à jour du mot de passe.");
                }
            } else {
                JOptionPane.showMessageDialog(mainWindow, "Les réponses ne correspondent pas.");
            }
        });
    }
}
