package View;


import View.panelKasir.panelKasir;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private panelKasir kasirPanel; // simpan referensi kasirPanel

    public mainFrame() {
        // Konfigurasi dasar frame
        setTitle("Canteen Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        // Inisialisasi panel
        initializePanels();

        // Set layout utama
        getContentPane().add(cardPanel, BorderLayout.CENTER);

        // Tampilkan dashboard pertama kali
        showDashboard();
    }

    private void initializePanels() {
        // Tambahkan semua panel dengan konsisten
        cardPanel.add(new panelDashboard(this, cardLayout, cardPanel), "DASHBOARD");
        cardPanel.add(new panelMenu(this), "MENU");
        cardPanel.add(new panelKategori(this), "CATEGORY");
        cardPanel.add(new panelSupplier(), "SUPPLIER");
        cardPanel.add(new panelPayment(), "PAYMENT");
        kasirPanel = new panelKasir(); // simpan referensinya
        cardPanel.add(kasirPanel, "KASIR");

    }

    public void showDashboard() {
        cardLayout.show(cardPanel, "DASHBOARD");
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName.toUpperCase());
    }

    public panelKasir getKasirPanel() {
        return kasirPanel;
    }

    public void logout() {
        // Implementasi logout
        this.dispose();
        new loginFrame().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginFrame().setVisible(true);
        });
    }
}