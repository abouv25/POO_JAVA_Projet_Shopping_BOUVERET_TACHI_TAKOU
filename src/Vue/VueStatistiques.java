package Vue;

import DAO.FactureDAO;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Map;

public class VueStatistiques extends JPanel {

    private JComboBox<Integer> comboAnnee;
    private JComboBox<String> comboMois;
    private JPanel panelGraphiques;
    private Map<String, Double> totalParDate;
    private Map<String, Double> totalParUser;
    private Map<String, Double> totalParProduit;
    private JFreeChart chartDate;
    private JFreeChart chartUser;
    private JFreeChart chartProduit;

    public VueStatistiques(MainWindow mainWindow) {
        setLayout(new BorderLayout());

        // ✅ Barre supérieure
        add(ComposantsUI.creerBarreSuperieure(mainWindow), BorderLayout.NORTH);

        // ✅ Titre
        JLabel titre = new JLabel("\uD83D\uDCCA Statistiques & Reporting", SwingConstants.CENTER);
        titre.setFont(new Font("SansSerif", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(titre, BorderLayout.NORTH);

        // ✅ Filtres
        JPanel filtres = new JPanel(new FlowLayout());

        comboAnnee = new JComboBox<>();
        int anneeActuelle = Year.now().getValue();
        for (int i = anneeActuelle; i >= anneeActuelle - 5; i--) {
            comboAnnee.addItem(i);
        }

        comboMois = new JComboBox<>(new String[]{
                "01 - Janvier", "02 - Février", "03 - Mars", "04 - Avril",
                "05 - Mai", "06 - Juin", "07 - Juillet", "08 - Août",
                "09 - Septembre", "10 - Octobre", "11 - Novembre", "12 - Décembre"
        });

        JButton boutonFiltrer = new JButton("Appliquer le filtre");
        boutonFiltrer.addActionListener(e -> chargerGraphiques(mainWindow));

        filtres.add(new JLabel("Année :"));
        filtres.add(comboAnnee);
        filtres.add(new JLabel("Mois :"));
        filtres.add(comboMois);
        filtres.add(boutonFiltrer);

        add(filtres, BorderLayout.CENTER);

        // ✅ Zone des graphiques
        panelGraphiques = new JPanel();
        panelGraphiques.setLayout(new BoxLayout(panelGraphiques, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelGraphiques);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.SOUTH);

        // ✅ Bas
        JPanel bas = new JPanel(new BorderLayout());

        JButton retour = new JButton("⬅ Retour");
        retour.addActionListener(e -> mainWindow.retourPagePrecedente());

        JButton exportPDF = new JButton("📥 Exporter PDF");
        exportPDF.addActionListener(e -> exporterPDF());

        JPanel gauche = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gauche.add(retour);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        droite.add(exportPDF);

        bas.add(gauche, BorderLayout.WEST);
        bas.add(droite, BorderLayout.EAST);

        add(bas, BorderLayout.PAGE_END);

        comboAnnee.setSelectedItem(anneeActuelle);
        comboMois.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        chargerGraphiques(mainWindow);
    }

    private void chargerGraphiques(MainWindow mainWindow) {
        panelGraphiques.removeAll();
        FactureDAO dao = new FactureDAO();

        int annee = (Integer) comboAnnee.getSelectedItem();
        int mois = comboMois.getSelectedIndex() + 1;

        totalParDate = dao.totalVentesParDate(annee, mois);
        totalParUser = dao.totalVentesParUtilisateur(annee, mois);
        totalParProduit = dao.totalVentesParProduit(annee, mois);

        DefaultCategoryDataset datasetDate = new DefaultCategoryDataset();
        for (String date : totalParDate.keySet()) {
            datasetDate.addValue(totalParDate.get(date), "Ventes", date);
        }
        chartDate = ChartFactory.createBarChart(
                "Ventes par jour (" + mois + "/" + annee + ")", "Date", "Total (€)", datasetDate);
        panelGraphiques.add(new ChartPanel(chartDate));

        DefaultCategoryDataset datasetUser = new DefaultCategoryDataset();
        for (String nom : totalParUser.keySet()) {
            datasetUser.addValue(totalParUser.get(nom), "Ventes utilisateur", nom);
        }
        chartUser = ChartFactory.createBarChart(
                "Ventes par utilisateur (" + mois + "/" + annee + ")", "Utilisateur", "Total (€)", datasetUser);
        panelGraphiques.add(Box.createVerticalStrut(20));
        panelGraphiques.add(new ChartPanel(chartUser));

        DefaultCategoryDataset datasetProduit = new DefaultCategoryDataset();
        for (String produit : totalParProduit.keySet()) {
            datasetProduit.addValue(totalParProduit.get(produit), "Ventes produit", produit);
        }
        chartProduit = ChartFactory.createBarChart(
                "Ventes par produit (" + mois + "/" + annee + ")", "Produit", "Total (€)", datasetProduit);
        panelGraphiques.add(Box.createVerticalStrut(20));
        panelGraphiques.add(new ChartPanel(chartProduit));

        panelGraphiques.revalidate();
        panelGraphiques.repaint();
    }

    private void exporterPDF() {
        try {
            String horodatage = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String path = "StatistiquesFiltres_" + horodatage + ".pdf";
            File fichierPDF = new File(path);

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fichierPDF));
            document.open();

            Paragraph titre = new Paragraph("Statistiques filtrées - BTT Shopping",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);

            document.add(new Paragraph("Généré le : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            document.add(new Paragraph(" "));

            BufferedImage img1 = chartDate.createBufferedImage(500, 300);
            BufferedImage img2 = chartUser.createBufferedImage(500, 300);
            BufferedImage img3 = chartProduit.createBufferedImage(500, 300);

            document.add(Image.getInstance(img1, null));
            document.add(Image.getInstance(img2, null));
            document.add(Image.getInstance(img3, null));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("📄 Tableau ventes par utilisateur", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tableUser = new PdfPTable(2);
            tableUser.setWidthPercentage(100);
            tableUser.addCell("Utilisateur");
            tableUser.addCell("Total (€)");
            for (Map.Entry<String, Double> entry : totalParUser.entrySet()) {
                tableUser.addCell(entry.getKey());
                tableUser.addCell(String.format("%.2f", entry.getValue()));
            }
            document.add(tableUser);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("📦 Tableau ventes par produit", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            PdfPTable tableProduit = new PdfPTable(2);
            tableProduit.setWidthPercentage(100);
            tableProduit.addCell("Produit");
            tableProduit.addCell("Total (€)");
            for (Map.Entry<String, Double> entry : totalParProduit.entrySet()) {
                tableProduit.addCell(entry.getKey());
                tableProduit.addCell(String.format("%.2f", entry.getValue()));
            }
            document.add(tableProduit);

            document.add(new Paragraph(" "));
            Paragraph merci = new Paragraph("Merci d'utiliser BTT Shopping !", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
            merci.setAlignment(Element.ALIGN_CENTER);
            document.add(merci);

            document.close();

            JOptionPane.showMessageDialog(this, "PDF exporté avec succès :\n" + path);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(fichierPDF);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur export PDF : " + ex.getMessage());
        }
    }
}
