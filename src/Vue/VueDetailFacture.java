package Vue;

import DAO.FactureDAO;
import modele.LignePanier;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VueDetailFacture extends JPanel {

    private JTable tableLignes;
    private JLabel labelTotal;
    private JButton boutonExporter;
    private JButton boutonRetour;
    private Utilisateur utilisateur;
    private MainWindow mainWindow;
    private int idFacture;

    public VueDetailFacture(int idFacture, Utilisateur utilisateur, MainWindow mainWindow) {
        this.idFacture = idFacture;
        this.utilisateur = utilisateur;
        this.mainWindow = mainWindow;

        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        // ✅ Barre du haut avec logo et utilisateur
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Tableau
        tableLignes = new JTable();
        tableLignes.setRowHeight(28);
        add(new JScrollPane(tableLignes), BorderLayout.CENTER);

        // ✅ Bas
        JPanel bas = new JPanel(new BorderLayout());
        bas.setBackground(getBackground());

        // ↩️ Retour
        boutonRetour = new JButton("⬅️ Retour");
        StyleUI.styliserBouton(boutonRetour);
        boutonRetour.addActionListener(e -> mainWindow.retourPagePrecedente());
        bas.add(boutonRetour, BorderLayout.WEST);

        // ➡️ Total + Export
        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        droite.setOpaque(false);

        labelTotal = new JLabel("Total : ");
        droite.add(labelTotal);

        boutonExporter = new JButton("Exporter en PDF");
        StyleUI.styliserBouton(boutonExporter);
        droite.add(boutonExporter);

        bas.add(droite, BorderLayout.EAST);
        add(bas, BorderLayout.SOUTH);

        boutonExporter.addActionListener(e -> exporterPDF());

        // ✅ Charger données
        chargerLignes();
    }

    private void chargerLignes() {
        FactureDAO dao = new FactureDAO();
        List<LignePanier> lignes = dao.listerLignesParFacture(idFacture);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix Unitaire", "Quantité", "Sous-total"}, 0);

        double total = 0.0;
        for (LignePanier l : lignes) {
            double sousTotal = l.getPrix() * l.getQuantite();
            model.addRow(new Object[]{
                    l.getNomProduit(),
                    String.format("%.2f €", l.getPrix()),
                    l.getQuantite(),
                    String.format("%.2f €", sousTotal)
            });
            total += sousTotal;
        }

        tableLignes.setModel(model);
        labelTotal.setText("Total : " + String.format("%.2f", total) + " €");
    }

    private void exporterPDF() {
        try {
            FactureDAO dao = new FactureDAO();
            List<LignePanier> lignes = dao.listerLignesParFacture(idFacture);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer la facture en PDF");
            fileChooser.setSelectedFile(new java.io.File("facture_" + idFacture + ".pdf"));

            int res = fileChooser.showSaveDialog(this);
            if (res != JFileChooser.APPROVE_OPTION) return;

            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".pdf")) {
                path += ".pdf";
            }

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(path));
            doc.open();

            Paragraph titre = new Paragraph("🛒 BTT Shopping - Facture", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLDITALIC));
            titre.setAlignment(Element.ALIGN_CENTER);
            doc.add(titre);
            doc.add(new Paragraph(" "));

            Paragraph infos = new Paragraph("Facture n° " + idFacture + "\nClient : " + utilisateur.getNom(), new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14));
            infos.setAlignment(Element.ALIGN_CENTER);
            doc.add(infos);
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Produit");
            table.addCell("Prix");
            table.addCell("Quantité");
            table.addCell("Sous-total");

            for (LignePanier l : lignes) {
                table.addCell(l.getNomProduit());
                table.addCell(String.format("%.2f €", l.getPrix()));
                table.addCell(String.valueOf(l.getQuantite()));
                double sousTotal = l.getPrix() * l.getQuantite();
                table.addCell(String.format("%.2f €", sousTotal));
            }

            doc.add(table);
            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total TTC : " + labelTotal.getText().replace("Total : ", "") + " €"));

            doc.add(new Paragraph(" "));
            Paragraph merci = new Paragraph("Merci de votre commande !", new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.ITALIC));
            merci.setAlignment(Element.ALIGN_CENTER);
            doc.add(merci);

            doc.close();

            JOptionPane.showMessageDialog(this, "PDF exporté avec succès !");
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(path));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export PDF : " + ex.getMessage());
        }
    }
}
