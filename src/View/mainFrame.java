package View;

import CRUD.repoSupplier;
import View.panelKasir.panelKasir;
import CRUD.repoKategori;
import CRUD.repoMenu;
import CRUD.repoTransaksi;
import View.panelReport.panelReport;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private panelKasir kasirPanel;
    private panelMenu menuPanelInstance;
    private panelKategori kategoriPanelInstance;
    private panelSupplier supplierPanelInstance;
    private panelReport reportPanel;
    private panelPayment paymentPanel;

    private repoKategori kategoriRepo;
    private repoMenu menuRepo;
    private repoSupplier supplierRepo;
    private repoTransaksi transaksiRepo;

    public mainFrame() {
        setTitle("Canteen Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);

        // --- INISIALISASI REPO DI SINI ---
        kategoriRepo = new repoKategori();
        supplierRepo = new repoSupplier(kategoriRepo);
        menuRepo = new repoMenu(kategoriRepo);
        transaksiRepo = new repoTransaksi();
        reportPanel = new panelReport(cardLayout, cardPanel);

        paymentPanel = new panelPayment();

        initializePanels();
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        showDashboard();
    }

    private void initializePanels() {
        cardPanel.add(new panelDashboard(this, cardLayout, cardPanel), "DASHBOARD");

        menuPanelInstance = new panelMenu(this, menuRepo, kategoriRepo, supplierRepo);
        kategoriPanelInstance = new panelKategori(this, kategoriRepo);
        supplierPanelInstance = new panelSupplier(this, supplierRepo, kategoriRepo);

        cardPanel.add(menuPanelInstance, "MENU");
        cardPanel.add(kategoriPanelInstance, "CATEGORY");
        cardPanel.add(supplierPanelInstance, "SUPPLIER");
        cardPanel.add(paymentPanel, "PAYMENT");

        kasirPanel = new panelKasir(menuRepo, transaksiRepo, reportPanel, paymentPanel);
        cardPanel.add(kasirPanel, "KASIR");
        cardPanel.add(reportPanel, "REPORT");
    }

    public void showDashboard() {
        cardLayout.show(cardPanel, "DASHBOARD");
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName.toUpperCase());
        switch (panelName.toUpperCase()) {
            case "KASIR":
                if (kasirPanel != null) {
                    kasirPanel.refreshMenuPanel();
                    kasirPanel.updatePaymentButtonsStatus(); // Panggil ini saat menampilkan panel kasir
                }
                break;
            case "MENU":
                if (menuPanelInstance != null) menuPanelInstance.refreshMenuData();
                break;
            case "CATEGORY":
                if (kategoriPanelInstance != null) kategoriPanelInstance.refreshCategoryData();
                break;
            case "SUPPLIER":
                if (supplierPanelInstance != null) supplierPanelInstance.refreshSupplierData();
                break;
            case "PAYMENT":
                // Tidak ada yang perlu diperbarui di KASIR saat beralih ke panel PAYMENT,
                // karena perubahan akan dipicu saat kembali ke KASIR.
                break;
        }
    }

    public panelKasir getKasirPanel() {
        return kasirPanel;
    }

    public panelMenu getMenuPanel() {
        return menuPanelInstance;
    }

    public panelSupplier getSupplierPanel() {
        return supplierPanelInstance;
    }

    public void logout() {
        this.dispose();
        new loginFrame().setVisible(true); // Asumsi Anda memiliki loginFrame
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Anda mungkin perlu menginisialisasi loginFrame atau langsung mainFrame tergantung alur aplikasi Anda
            new loginFrame().setVisible(true); // Atau new mainFrame().setVisible(true);
        });
    }
}