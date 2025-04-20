package Vue;

import DAO.FactureDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VueHistoriqueCommandes extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField champRecherche;
    private JLabel labelTotal;
    private JButton boutonExporter;
    private JLabel labelPage;
    private int pageActuelle = 1;
    private final int lignesParPage = 10;
    private List<Facture> factures;
    private MainWindow mainWindow;

    public VueHistoriqueCommandes(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        Utilisateur utilisateur = mainWindow.getUtilisateurConnecte();
        if (utilisateur == null) {
            mainWindow.switchTo("connexion");
            return;
        }

        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        JLabel titre = new JLabel("📄 Historique de vos commandes", SwingConstants.CENTER);
        StyleUI.styliserTitre(titre);
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Date", "N° Facture", "Total", "Remise (€)"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        StyleUI.appliquerStyleTableau(table);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        StyleUI.appliquerFondEtCadre(scrollPane);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bas = new JPanel(new BorderLayout());
        StyleUI.appliquerFondEtCadre(bas);

        // --- Recherche ---
        champRecherche = new JTextField(10);
        JButton boutonToutVoir = new JButton("Tout voir");
        boutonExporter = new JButton("Exporter PDF");

        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        StyleUI.appliquerFondEtCadre(recherchePanel);
        recherchePanel.add(new JLabel("🔍 Rechercher N° : "));
        recherchePanel.add(champRecherche);
        recherchePanel.add(boutonToutVoir);
        recherchePanel.add(boutonExporter);

        champRecherche.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filtrer(); }
            public void removeUpdate(DocumentEvent e) { filtrer(); }
            public void changedUpdate(DocumentEvent e) { filtrer(); }
        });
        boutonToutVoir.addActionListener(e -> champRecherche.setText(""));

        // --- Total global ---
        labelTotal = new JLabel("Total global : 0.00 €");
        StyleUI.styliserTexte(labelTotal);
        JPanel totalPanel = new JPanel();
        StyleUI.appliquerFondEtCadre(totalPanel);
        totalPanel.add(labelTotal);

        // --- Bouton retour ---
        JButton retour = new JButton("⬅ Retour");
        retour.addActionListener(e -> mainWindow.retourPagePrecedente());
        JPanel retourPanel = new JPanel();
        StyleUI.appliquerFondEtCadre(retourPanel);
        retourPanel.add(retour);

        // --- Pagination ---
        JButton prec = new JButton("◀");
        JButton suiv = new JButton("▶");
        labelPage = new JLabel("Page 1");
        JPanel paginationPanel = new JPanel();
        StyleUI.appliquerFondEtCadre(paginationPanel);
        paginationPanel.add(prec);
        paginationPanel.add(labelPage);
        paginationPanel.add(suiv);

        prec.addActionListener(e -> {
            if (pageActuelle > 1) {
                pageActuelle--;
                chargerFactures(mainWindow.getUtilisateurConnecte());
            }
        });
        suiv.addActionListener(e -> {
            int total = model.getRowCount();
            if (pageActuelle * lignesParPage < factures.size()) {
                pageActuelle++;
                chargerFactures(mainWindow.getUtilisateurConnecte());
            }
        });

        JPanel centreBas = new JPanel(new BorderLayout());
        centreBas.add(totalPanel, BorderLayout.WEST);
        centreBas.add(paginationPanel, BorderLayout.EAST);

        bas.add(recherchePanel, BorderLayout.WEST);
        bas.add(centreBas, BorderLayout.CENTER);
        bas.add(retourPanel, BorderLayout.EAST);
        add(bas, BorderLayout.SOUTH);

        // --- Tooltips ---
        table.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    int modelRow = table.convertRowIndexToModel(row);
                    String date = model.getValueAt(modelRow, 0).toString();
                    String num = model.getValueAt(modelRow, 1).toString();
                    String total = model.getValueAt(modelRow, 2).toString();
                    String remise = model.getValueAt(modelRow, 3).toString();
                    table.setToolTipText("Facture n°" + num + " du " + date + "\nTotal : " + total + " | Remise : " + remise);
                } else {
                    table.setToolTipText(null);
                }
            }
        });

        // --- Double-clic pour voir les détails ---
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    int idFacture = Integer.parseInt(model.getValueAt(row, 1).toString());
                    mainWindow.ajouterVue("detailFacture", new VueDetailFacture(idFacture, utilisateur, mainWindow));
                    mainWindow.switchTo("detailFacture");
                }
            }
        });

        // --- Export PDF ---
        boutonExporter.addActionListener(e -> exporterPDF(utilisateur));
        chargerFactures(utilisateur);
    }

    private void filtrer() {
        pageActuelle = 1;
        chargerFactures(mainWindow.getUtilisateurConnecte());
    }

    private void chargerFactures(Utilisateur utilisateur) {
        FactureDAO dao = new FactureDAO();
        factures = dao.listerFacturesPourUtilisateur(utilisateur);

        String texte = champRecherche.getText().trim().toLowerCase();
        List<Facture> filtres = factures.stream()
                .filter(f -> texte.isEmpty() || ("" + f.getId()).contains(texte))
                .toList();

        int debut = (pageActuelle - 1) * lignesParPage;
        int fin = Math.min(debut + lignesParPage, filtres.size());
        List<Facture> page = filtres.subList(debut, fin);

        model.setRowCount(0);
        double totalGlobal = 0.0;

        for (Facture f : page) {
            double total = f.getMontantTotal();
            double remise = f.getLignes() != null
                    ? f.getLignes().stream().mapToDouble(l -> l.getPrix() * l.getQuantite()).sum() * f.getRemisePourcent() / 100
                    : 0;

            model.addRow(new Object[]{
                    f.getDateFormatee(),
                    f.getId(),
                    String.format("%.2f €", total),
                    String.format("%.2f €", remise)
            });

            totalGlobal += total;
        }

        labelTotal.setText("Total global : " + String.format("%.2f", totalGlobal) + " €");
        int totalPages = (int) Math.ceil((double) filtres.size() / lignesParPage);
        labelPage.setText("Page " + pageActuelle + " / " + (totalPages == 0 ? 1 : totalPages));
    }

    private void exporterPDF(Utilisateur utilisateur) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exporter les factures (PDF)");
            fileChooser.setSelectedFile(new java.io.File("factures_" + utilisateur.getNom() + ".pdf"));

            if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".pdf")) path += ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            document.add(new Paragraph("Historique des commandes de : " + utilisateur.getNom()));
            document.add(new Paragraph(" "));

            PdfPTable tablePDF = new PdfPTable(4);
            tablePDF.setWidthPercentage(100);
            tablePDF.addCell("Date");
            tablePDF.addCell("N° Facture");
            tablePDF.addCell("Total");
            tablePDF.addCell("Remise");

            for (int i = 0; i < model.getRowCount(); i++) {
                tablePDF.addCell(model.getValueAt(i, 0).toString());
                tablePDF.addCell(model.getValueAt(i, 1).toString());
                tablePDF.addCell(model.getValueAt(i, 2).toString());
                tablePDF.addCell(model.getValueAt(i, 3).toString());
            }

            document.add(tablePDF);
            document.add(new Paragraph(" "));
            document.add(new Paragraph(labelTotal.getText()));
            document.close();

            JOptionPane.showMessageDialog(this, "PDF exporté dans :\n" + path);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur export PDF : " + ex.getMessage());
        }
    }
}
