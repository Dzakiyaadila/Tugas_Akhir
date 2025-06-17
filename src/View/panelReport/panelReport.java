package View.panelReport;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class panelReport extends JPanel {
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField filterField;
    private final CardLayout cardLayout;
    private final JPanel cardPanel;

    public panelReport(CardLayout cardLayout, JPanel cardPanel) {
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // === TOP: Filter Panel ===
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(230, 240, 255));

        filterField = new JTextField(10);
        JButton filterButton = new JButton("Filter");
        styleButton(filterButton, new Color(0, 102, 204), Color.WHITE);

        topPanel.add(new JLabel("Tanggal (YYYY-MM-DD):"));
        topPanel.add(filterField);
        topPanel.add(filterButton);

        // === TABLE: Isi Data ===
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"UserID", "Waktu", "Metode", "Menu", "Jumlah", "Subtotal", "Total"});
        table = new JTable(model);
        JScrollPane tableScroll = new JScrollPane(table);

        // === BOTTOM: Tombol Export & Back ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(230, 240, 255));

        JButton backButton = new JButton("← Kembali");
        styleButton(backButton, Color.GRAY, Color.WHITE);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "DASHBOARD"));

        JButton exportButton = new JButton("Export");
        styleButton(exportButton, new Color(0, 120, 215), Color.WHITE);

        bottomPanel.add(backButton);
        bottomPanel.add(exportButton);

        // === Tambahkan ke panel utama ===
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // === Aksi Tombol ===
        filterButton.addActionListener(e -> loadData(filterField.getText()));
        exportButton.addActionListener(e -> exportToCSV());

        // Load semua data awal
        loadData("");

    }

    private void loadData(String tanggal) {
        model.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("data/riwayat_transaksi.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 7 && (tanggal.isEmpty() || data[1].startsWith(tanggal))) {
                    model.addRow(data);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal membaca riwayat_transaksi.csv", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refresh() {
        loadData(filterField.getText());
    }


    private void exportToCSV() {
        try (FileWriter fw = new FileWriter("data/laporan_export.csv")) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    fw.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");
            }
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke laporan_export.csv");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menulis ke laporan_export.csv", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color bg, Color fg) {
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
    }
}
