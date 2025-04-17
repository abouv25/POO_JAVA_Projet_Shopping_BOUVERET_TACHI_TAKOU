package Vue;

import DAO.UtilisateurDAO;
import modele.Utilisateur;

import javax.swing.*;
import java.awt.*;

public class ModifierUtilisateurDialog extends JDialog {

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JCheckBox checkAdmin;
    private JCheckBox checkFidele;
    private JPasswordField champMotDePasse;
    private boolean modifie = false;

    public ModifierUtilisateurDialog(JFrame parent, Utilisateur utilisateur) {
        super(parent, "Modifier l'utilisateur", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        champNom = new JTextField(utilisateur.getNom());
        champPrenom = new JTextField(utilisateur.getPrenom());
        champEmail = new JTextField(utilisateur.getEmail());
        champMotDePasse = new JPasswordField(utilisateur.getMotDePasse());
        checkAdmin = new JCheckBox("Admin", utilisateur.isEstAdmin());
        checkFidele = new JCheckBox("Client fidèle", utilisateur.isClientFidele());

        panel.add(new JLabel("Nom :"));
        panel.add(champNom);
        panel.add(new JLabel("Prénom :"));
        panel.add(champPrenom);
        panel.add(new JLabel("Email :"));
        panel.add(champEmail);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(champMotDePasse);
        panel.add(checkAdmin);
        panel.add(checkFidele);

        JButton boutonValider = new JButton("Enregistrer");
        JButton boutonAnnuler = new JButton("Annuler");

        boutonValider.addActionListener(e -> {
            utilisateur.setNom(champNom.getText().trim());
            utilisateur.setPrenom(champPrenom.getText().trim());
            utilisateur.setEmail(champEmail.getText().trim());
            utilisateur.setMotDePasse(new String(champMotDePasse.getPassword()));
            utilisateur.setEstAdmin(checkAdmin.isSelected());
            utilisateur.setClientFidele(checkFidele.isSelected());

            UtilisateurDAO dao = new UtilisateurDAO();
            if (dao.modifierUtilisateur(utilisateur)) {
                JOptionPane.showMessageDialog(this, "Utilisateur modifié avec succès.");
                modifie = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la modification.");
            }
        });

        boutonAnnuler.addActionListener(e -> dispose());

        JPanel boutons = new JPanel();
        boutons.add(boutonValider);
        boutons.add(boutonAnnuler);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(boutons, BorderLayout.SOUTH);
    }

    public boolean isModifie() {
        return modifie;
    }
}
