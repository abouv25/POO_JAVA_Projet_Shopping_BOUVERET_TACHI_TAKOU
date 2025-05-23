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

public class VueDetailFacture extends JFrame {

    private JTable tableLignes;
    private JLabel labelTotal;
    private JButton boutonExporter;
    private Utilisateur utilisateur;

    public VueDetailFacture(int idFacture, Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Détails de la facture #" + idFacture);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        tableLignes = new JTable();
        labelTotal = new JLabel("Total : ");
        boutonExporter = new JButton("Exporter en PDF");

        JPanel bas = new JPanel(new BorderLayout());
        bas.add(labelTotal, BorderLayout.WEST);
        bas.add(boutonExporter, BorderLayout.EAST);

        add(new JScrollPane(tableLignes), BorderLayout.CENTER);
        add(bas, BorderLayout.SOUTH);

        chargerLignes(idFacture);

        boutonExporter.addActionListener(e -> exporterPDF(idFacture));
    }

    private void chargerLignes(int idFacture) {
        FactureDAO dao = new FactureDAO();
        List<LignePanier> lignes = dao.listerLignesParFacture(idFacture);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Produit", "Prix", "Quantité", "Sous-total"}, 0);

        double total = 0.0;
        for (LignePanier l : lignes) {
            double sousTotal = l.getPrix() * l.getQuantite();
            model.addRow(new Object[]{
                    l.getNomProduit(),
                    String.format("%.2f", l.getPrix()) + " €",
                    l.getQuantite(),
                    String.format("%.2f", sousTotal) + " €"
            });
            total += sousTotal;
        }

        tableLignes.setModel(model);
        labelTotal.setText("Total : " + String.format("%.2f", total) + " €");
    }

    private void exporterPDF(int idFacture) {
        try {
            FactureDAO dao = new FactureDAO();
            List<LignePanier> lignes = dao.listerLignesParFacture(idFacture);

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Enregistrer la facture au format PDF");
            fileChooser.setSelectedFile(new java.io.File("facture_" + idFacture + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(this);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            java.io.File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();

            com.lowagie.text.Font logoFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 20, com.lowagie.text.Font.BOLDITALIC, Color.DARK_GRAY);
            Paragraph logo = new Paragraph("🛒 shopping B-T-T ING3", logoFont);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);

            doc.add(new Paragraph(" "));

            com.lowagie.text.Font titreFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 16, com.lowagie.text.Font.BOLD);
            Paragraph titre = new Paragraph("FACTURE", titreFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            doc.add(titre);

            doc.add(new Paragraph(" "));

            doc.add(new Paragraph("Numéro de facture : " + idFacture));
            doc.add(new Paragraph("Date : " + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))));
            doc.add(new Paragraph("Client : " + utilisateur.getNom()));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.addCell("Produit");
            table.addCell("Prix unitaire");
            table.addCell("Quantité");
            table.addCell("Sous-total");

            double total = 0.0;
            for (LignePanier l : lignes) {
                table.addCell(l.getNomProduit());
                table.addCell(String.format("%.2f €", l.getPrix()));
                table.addCell(String.valueOf(l.getQuantite()));
                double sousTotal = l.getPrix() * l.getQuantite();
                table.addCell(String.format("%.2f €", sousTotal));
                total += sousTotal;
            }

            doc.add(table);

            doc.add(new Paragraph(" "));
            doc.add(new Paragraph("Total à payer : " + String.format("%.2f", total) + " €"));

            doc.add(new Paragraph(" "));
            com.lowagie.text.Font merciFont = new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 14, com.lowagie.text.Font.ITALIC);
            Paragraph merci = new Paragraph("Merci de votre commande !", merciFont);
            merci.setAlignment(Element.ALIGN_CENTER);
            doc.add(merci);

            doc.close();

            JOptionPane.showMessageDialog(this,
                    "PDF exporté avec succès :\n" + filePath,
                    "Export terminé", JOptionPane.INFORMATION_MESSAGE);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new java.io.File(filePath));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'export PDF : " + e.getMessage(),
                    "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
