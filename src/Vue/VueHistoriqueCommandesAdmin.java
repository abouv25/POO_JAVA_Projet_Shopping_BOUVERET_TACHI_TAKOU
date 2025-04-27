package Vue;

import DAO.FactureDAO;
import DAO.UtilisateurDAO;
import modele.Facture;
import modele.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.time.Year;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class VueHistoriqueCommandesAdmin extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField champRechercheNom;
    private JTextField champRechercheID;
    private JComboBox<Integer> comboAnnee;
    private JButton boutonExporterPDF;
    private JLabel labelPage;
    private int pageActuelle = 1;
    private int lignesParPage = 10;
    private List<Facture> facturesGlobales;

    private List<Facture> facturesFiltrees;

    public VueHistoriqueCommandesAdmin(MainWindow mainWindow) {
        setLayout(new BorderLayout());
        StyleUI.appliquerFondEtCadre(this);

        JLabel titre = new JLabel("\uD83D\uDCCA Historique global des commandes", SwingConstants.CENTER);
        StyleUI.styliserTitre(titre);
        add(titre, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Date", "ID Facture", "Client", "Total", "Remise"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(model) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                double total = Double.parseDouble(getValueAt(row, 3).toString().replace(" €", ""));
                double remise = Double.parseDouble(getValueAt(row, 4).toString().replace(" €", ""));
                if (remise > 0) {
                    c.setBackground(new Color(204, 229, 255)); // bleu clair
                } else if (total > 100) {
                    c.setBackground(new Color(204, 255, 204)); // vert clair
                } else {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        };

        StyleUI.appliquerStyleTableau(table);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panelFiltres = new JPanel(new FlowLayout(FlowLayout.LEFT));
        StyleUI.appliquerFondEtCadre(panelFiltres);

        champRechercheNom = new JTextField(10);
        champRechercheID = new JTextField(5);
        comboAnnee = new JComboBox<>();
        int anneeActuelle = Year.now().getValue();
        comboAnnee.addItem(0);
        for (int i = anneeActuelle; i >= anneeActuelle - 5; i--) comboAnnee.addItem(i);

        panelFiltres.add(new JLabel("Nom utilisateur :"));
        panelFiltres.add(champRechercheNom);
        panelFiltres.add(new JLabel("ID facture :"));
        panelFiltres.add(champRechercheID);
        panelFiltres.add(new JLabel("Année :"));
        panelFiltres.add(comboAnnee);

        JButton boutonFiltrer = new JButton("Filtrer");
        boutonFiltrer.addActionListener(e -> {
            pageActuelle = 1;
            chargerFactures();
        });
        panelFiltres.add(boutonFiltrer);

        JButton boutonToutVoir = new JButton("Tout voir");
        boutonToutVoir.addActionListener(e -> {
            champRechercheNom.setText("");
            champRechercheID.setText("");
            comboAnnee.setSelectedIndex(0);
            pageActuelle = 1;
            chargerFactures();
        });
        panelFiltres.add(boutonToutVoir);

        boutonExporterPDF = new JButton("Exporter PDF");
        boutonExporterPDF.addActionListener(e -> exporterPDF());
        panelFiltres.add(boutonExporterPDF);

        add(panelFiltres, BorderLayout.SOUTH);

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    int idFacture = Integer.parseInt(model.getValueAt(row, 1).toString());
                    String nomClient = model.getValueAt(row, 2).toString();
                    Utilisateur u = new UtilisateurDAO().getUtilisateurParNom(nomClient);
                    if (u != null) {
                        VueDetailFacture vueDetail = new VueDetailFacture(idFacture, u, mainWindow);
                        mainWindow.ajouterVue("detailFacture" + idFacture, vueDetail);
                        mainWindow.switchTo("detailFacture" + idFacture);
                    } else {
                        JOptionPane.showMessageDialog(VueHistoriqueCommandesAdmin.this, "Utilisateur introuvable pour cette facture.");
                    }
                }
            }
        });

        chargerFactures();
    }

    private void chargerFactures() {
        FactureDAO dao = new FactureDAO();
        facturesGlobales = dao.listerToutesLesFactures();

        String nom = champRechercheNom.getText().toLowerCase().trim();
        String idTexte = champRechercheID.getText().trim();
        int annee = (int) comboAnnee.getSelectedItem();

        facturesFiltrees = facturesGlobales.stream()
                .filter(f -> nom.isEmpty() || f.getClient().getNom().toLowerCase().contains(nom))
                .filter(f -> idTexte.isEmpty() || ("" + f.getId()).contains(idTexte))
                .filter(f -> annee == 0 || f.getDate().getYear() == annee)
                .toList();

        int debut = (pageActuelle - 1) * lignesParPage;
        int fin = Math.min(debut + lignesParPage, facturesFiltrees.size());
        List<Facture> page = facturesFiltrees.subList(debut, fin);

        model.setRowCount(0);
        for (Facture f : page) {
            double remise = f.getLignes() != null ? f.getLignes().stream().mapToDouble(l -> l.getPrix() * l.getQuantite()).sum() * f.getRemisePourcent() / 100 : 0;
            model.addRow(new Object[]{
                    f.getDateFormatee(),
                    f.getId(),
                    f.getClient().getNom(),
                    String.format("%.2f €", f.getMontantTotal()),
                    String.format("%.2f €", remise)
            });
        }

        int totalPages = (int) Math.ceil((double) facturesFiltrees.size() / lignesParPage);
        labelPage.setText("Page " + pageActuelle + " / " + (totalPages == 0 ? 1 : totalPages));
    }

    private void exporterPDF() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Exporter les factures affichées (PDF)");
            fileChooser.setSelectedFile(new java.io.File("historique_factures.pdf"));

            int result = fileChooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;

            String path = fileChooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".pdf")) path += ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            Paragraph titre = new Paragraph("Historique des factures", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
            titre.setAlignment(Element.ALIGN_CENTER);
            document.add(titre);
            document.add(new Paragraph(" "));

            PdfPTable tablePDF = new PdfPTable(5);
            tablePDF.setWidthPercentage(100);
            tablePDF.addCell("Date");
            tablePDF.addCell("ID Facture");
            tablePDF.addCell("Client");
            tablePDF.addCell("Total");
            tablePDF.addCell("Remise");

            for (Facture f : facturesFiltrees) {
                tablePDF.addCell(f.getDateFormatee());
                tablePDF.addCell(String.valueOf(f.getId()));
                tablePDF.addCell(f.getClient().getNom());
                tablePDF.addCell(String.format("%.2f €", f.getMontantTotal()));
                double remise = f.getLignes() != null ? f.getLignes().stream().mapToDouble(l -> l.getPrix() * l.getQuantite()).sum() * f.getRemisePourcent() / 100 : 0;
                tablePDF.addCell(String.format("%.2f €", remise));
            }

            document.add(tablePDF);
            document.close();

            JOptionPane.showMessageDialog(this, "PDF exporté avec succès :\n" + path);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur export PDF : " + e.getMessage());
        }
    }
}
