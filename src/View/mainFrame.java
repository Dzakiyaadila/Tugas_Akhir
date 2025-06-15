package View;

import CRUD.repoSupplier;
import View.panelKasir.panelKasir;
import CRUD.repoKategori;
import CRUD.repoMenu;
import CRUD.repoTransaksi;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private panelKasir kasirPanel;
    private panelMenu menuPanelInstance;
    private panelKategori kategoriPanelInstance;
    private panelSupplier supplierPanelInstance;

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
        kategoriRepo = new repoKategori(); // Inisialisasi repoKategori dulu
        supplierRepo = new repoSupplier(kategoriRepo);
        menuRepo = new repoMenu(kategoriRepo); // Inisialisasi repoMenu, passing kategoriRepo
        transaksiRepo = new repoTransaksi();

        initializePanels();
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        showDashboard();
    }

    private void initializePanels() {
        cardPanel.add(new panelDashboard(this, cardLayout, cardPanel), "DASHBOARD");

        menuPanelInstance = new panelMenu(this, menuRepo, kategoriRepo, supplierRepo);
        kategoriPanelInstance = new panelKategori(this, kategoriRepo);
        supplierPanelInstance = new panelSupplier(this, supplierRepo, kategoriRepo);
        menuPanelInstance = new panelMenu(this, menuRepo, kategoriRepo, supplierRepo);

        cardPanel.add(menuPanelInstance, "MENU");
        cardPanel.add(kategoriPanelInstance, "CATEGORY");
        cardPanel.add(supplierPanelInstance, "SUPPLIER");
        cardPanel.add(new panelPayment(), "PAYMENT");
        kasirPanel = new panelKasir(menuRepo, transaksiRepo);
        cardPanel.add(kasirPanel, "KASIR");
    }

    public void showDashboard() {
        cardLayout.show(cardPanel, "DASHBOARD");
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName.toUpperCase());
        if (panelName.equalsIgnoreCase("KASIR")) {
            if (kasirPanel != null) {
                kasirPanel.refreshMenuPanel();
            }
        } else if (panelName.equalsIgnoreCase("MENU")) {
            if (menuPanelInstance != null) {
                menuPanelInstance.refreshMenuData();
            }
        } else if (panelName.equalsIgnoreCase("CATEGORY")) {
            if (kategoriPanelInstance != null) {
                kategoriPanelInstance.refreshCategoryData();
            }
        } else if (panelName.equalsIgnoreCase("SUPPLIER")) {
            if (supplierPanelInstance != null) {
                supplierPanelInstance.refreshSupplierData();
            }
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