package View.panelKasir;

import Logic.*;
import View.mainFrame;
import CRUD.repoMenu;
import CRUD.repoTransaksi;
import CRUD.repoPaymentSettings;
import View.panelReport.panelReport;
import View.panelPayment; // Import panelPayment

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class panelKasir extends JPanel {
    private transient JPanel menuPanel;
    private JPanel orderPanel;
    private DefaultListModel<String> orderListModel;
    private JLabel totalLabel;
    private List<OrderItem> currentOrder;
    private repoMenu menuRepo;
    private repoTransaksi transaksiRepo;
    private panelReport reportPanel;
    private repoPaymentSettings paymentSettingsRepo;
    private panelPayment paymentPanel; // Field untuk menyimpan instance panelPayment

    private JButton cashButton; // Field untuk tombol Tunai
    private JButton qrisButton; // Field untuk tombol QRIS


    // Perbarui konstruktor untuk menerima panelPayment
    public panelKasir(repoMenu menuRepo, repoTransaksi transaksiRepo, panelReport reportPanel, repoPaymentSettings paymentSettingsRepo) {
        this.menuRepo = menuRepo;
        this.transaksiRepo = transaksiRepo;
        this.reportPanel = reportPanel;// Inisialisasi field paymentPanel
        this.paymentSettingsRepo = paymentSettingsRepo;

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        menuPanel = createMenuPanel();
        orderPanel = createOrderPanel(); // createOrderPanel akan menginisialisasi cashButton dan qrisButton

        JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, orderPanel);
        contentPanel.setResizeWeight(0.7);
        contentPanel.setDividerSize(10);
        contentPanel.setContinuousLayout(true);
        contentPanel.setBorder(null);
        add(contentPanel, BorderLayout.CENTER);

        add(createFooterPanel(), BorderLayout.SOUTH);

        currentOrder = new ArrayList<>();
        updatePaymentButtonsStatus(); // Panggil saat inisialisasi untuk mengatur status awal tombol
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("← Back");
        backButton.addActionListener(e -> ((mainFrame) SwingUtilities.getWindowAncestor(this)).showDashboard());
        panel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("KASIR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Menu Tersedia"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        List<Logic.menu> allMenus = menuRepo.getListMenu();
        System.out.println("PanelKasir: Jumlah menu yang dimuat dari repo: " + allMenus.size());

        Map<String, JPanel> categoryPanels = new LinkedHashMap<>();

        // Inisialisasi "All Items" panel dengan layout-nya
        JPanel allItemsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        allItemsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        categoryPanels.put("All Items", allItemsPanel);

        // Kumpulkan kategori unik dari menu yang dimuat
        Set<String> categories = allMenus.stream()
                .map(m -> m.getKategori().getNama())
                .collect(Collectors.toSet());

        // Inisialisasi panel untuk setiap kategori yang unik sebelum digunakan
        for (String category : categories) {
            JPanel categorySpecificPanel = new JPanel(new GridLayout(0, 3, 10, 10));
            categorySpecificPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            categoryPanels.putIfAbsent(category, categorySpecificPanel);
        }

        for (Logic.menu m : allMenus) {
            System.out.println("PanelKasir: Menambahkan menu '" + m.getNama() + "' ke GUI.");
            JPanel itemPanelForCategory = createMenuItemPanel(m);
            categoryPanels.get(m.getKategori().getNama()).add(itemPanelForCategory);

            JPanel itemPanelForAll = createMenuItemPanel(m);
            categoryPanels.get("All Items").add(itemPanelForAll);
        }

        tabbedPane.addTab("All Items", new JScrollPane(categoryPanels.get("All Items")));

        for (Map.Entry<String, JPanel> entry : categoryPanels.entrySet()) {
            if (!entry.getKey().equals("All Items")) {
                tabbedPane.addTab(entry.getKey(), new JScrollPane(entry.getValue()));
            }
        }

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMenuItemPanel(Logic.menu item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel nameLabel = new JLabel(item.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.add(nameLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        JLabel priceLabel = new JLabel("Harga: Rp " + String.format("%.2f", item.getHarga()));
        JLabel stockLabel = new JLabel("Stok: " + item.getStok() + " unit");
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        panel.add(infoPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("+ Tambah");
        styleButton(addButton, new Color(0, 180, 120), Color.WHITE);
        addButton.addActionListener(e -> {
            addToOrder(item);
        });

        panel.add(addButton, BorderLayout.SOUTH);
        return panel;
    }


    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Aktif"));

        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);

        orderList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int index = orderList.locationToIndex(e.getPoint());
                    if (index != -1) {
                        handleOrderItemClick(index);
                    }
                }
            }
        });

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        cashButton = new JButton("Tunai"); // Inisialisasi cashButton
        stylePaymentButton(cashButton, new Color(0, 180, 60));
        cashButton.addActionListener(e -> processPayment("TUNAI"));
        qrisButton = new JButton("QRIS"); // Inisialisasi qrisButton
        stylePaymentButton(qrisButton, new Color(0, 120, 215));
        qrisButton.addActionListener(e -> processPayment("QRIS"));

        buttonPanel.add(cashButton);
        buttonPanel.add(qrisButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
    }

    private void stylePaymentButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));
    }

    private void addToOrder(Logic.menu selectedMenu) {
        String itemName = selectedMenu.getNama();
        double harga = selectedMenu.getHarga();
        int stok = selectedMenu.getStok();

        if (stok <= 0) {
            JOptionPane.showMessageDialog(this, "Stok habis!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean found = false;
        for (OrderItem oi : currentOrder) {
            if (oi.getNama().equals(itemName)) {
                if (oi.getJumlah() < stok) {
                    oi.setJumlah(oi.getJumlah() + 1);
                    found = true;
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "Stok tidak cukup!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }
        if (!found) {
            currentOrder.add(new OrderItem(itemName, harga, 1));
        }
        refreshOrderList();
    }

    private void refreshOrderList() {
        orderListModel.clear();
        double total = 0;
        for (OrderItem oi : currentOrder) {
            orderListModel.addElement(oi.getNama() + " x" + oi.getJumlah() + " - Rp " + String.format("%.2f", (oi.getHarga() * oi.getJumlah())));
            total += oi.getHarga() * oi.getJumlah();
        }
        totalLabel.setText("Total: Rp " + String.format("%.2f", total));
    }

    private void resetOrder() {
        orderListModel.clear();
        currentOrder.clear();
        totalLabel.setText("Total: Rp 0");
    }

    private void processPayment(String paymentMethod) {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Order kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = currentOrder.stream().mapToDouble(oi -> oi.getHarga() * oi.getJumlah()).sum();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Total: Rp " + String.format("%.2f", total) +
                        "\nMetode: " + paymentMethod +
                        "\nSimpan transaksi?",
                "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String paymentId = "PAY-" + System.currentTimeMillis();
            payment paymentInstance = null;

            switch (paymentMethod) {
                case "TUNAI":
                    paymentInstance = new CashPayment(paymentId, total);
                    break;
                case "QRIS":
                    paymentInstance = new QRISPayment(paymentId, total);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Metode pembayaran tidak dikenal: " + paymentMethod, "Error Pembayaran", JOptionPane.ERROR_MESSAGE);
                    return;
            }

            if (paymentInstance != null) {
                paymentInstance.pembayaran();
                System.out.println("Status pembayaran untuk " + paymentMethod + ": " + paymentInstance.getStatus());

                if (paymentInstance.getStatus().equals("Completed")) {
                    for (OrderItem oi : currentOrder) {
                        menu itemMenu = menuRepo.getListMenu().stream()
                                .filter(m -> m.getNama().equals(oi.getNama()))
                                .findFirst()
                                .orElse(null);
                        if (itemMenu != null) {
                            menuRepo.updateStok(itemMenu.getId(), itemMenu.getStok() - oi.getJumlah());
                        }
                    }
                    saveTransaction(paymentMethod);
                    JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    resetOrder();
                    refreshMenuPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Pembayaran gagal diproses. Status: " + paymentInstance.getStatus(), "Error Pembayaran", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void saveTransaction(String metode) {
        int userId = 1;
        double total = currentOrder.stream().mapToDouble(oi -> oi.getHarga() * oi.getJumlah()).sum();

        List<transaksiDetail> detailList = new ArrayList<>();
        for (OrderItem oi : currentOrder) {
            Logic.menu associatedMenu = menuRepo.getListMenu().stream()
                    .filter(m -> m.getNama().equals(oi.getNama()))
                    .findFirst()
                    .orElse(null);
            if (associatedMenu != null) {
                detailList.add(new transaksiDetail(String.valueOf(associatedMenu.getId()), oi.getJumlah(), oi.getHarga() * oi.getJumlah()));
            } else {
                System.err.println("Menu " + oi.getNama() + " tidak ditemukan saat menyimpan transaksi.");
            }
        }
        Logic.transaksi trx = new Logic.transaksi(userId, total, metode, detailList);
        transaksiRepo.saveTransaksi(trx);
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBackground(new Color(240, 240, 240));

        JButton resetButton = new JButton("Reset Order");
        styleButton(resetButton, new Color(200, 50, 50), Color.WHITE);
        resetButton.addActionListener(e -> resetOrder());

        panel.add(resetButton);
        return panel;
    }

    @Override
    public void addNotify() {
        super.addNotify();
    }

    public void refreshMenuPanel() {
        System.out.println("refreshMenuPanel() called in panelKasir. Rebuilding menu UI.");

        Component centerComponent = getComponent(1);

        if (centerComponent instanceof JSplitPane splitPane) {
            Component existingLeftComponent = splitPane.getLeftComponent();
            if (existingLeftComponent != null) {
                splitPane.remove(existingLeftComponent);
            }

            menuPanel = createMenuPanel();

            splitPane.setLeftComponent(menuPanel);

            splitPane.revalidate();
            splitPane.repaint();
            revalidate();
            repaint();

            System.out.println("Menu Panel in JSplitPane rebuilt and repainted.");
        } else {
            System.err.println("Error: Central component is not a JSplitPane in refreshMenuPanel!");
        }
        updatePaymentButtonsStatus(); // Panggil di sini juga
    }

    // Metode baru untuk memperbarui status tombol pembayaran
    public void updatePaymentButtonsStatus() {
        if (cashButton != null) {
            boolean cashActive = paymentSettingsRepo.isMethodActive("TUNAI");
            cashButton.setEnabled(cashActive);
            if (cashActive) {
                stylePaymentButton(cashButton, new Color(0, 180, 60));
            } else {
                stylePaymentButton(cashButton, new Color(150, 150, 150));
            }
        }
        if (qrisButton != null) {
            boolean qrisActive = paymentSettingsRepo.isMethodActive("QRIS");
            qrisButton.setEnabled(qrisActive);
            if (qrisActive) {
                stylePaymentButton(qrisButton, new Color(0, 120, 215));
            } else {
                stylePaymentButton(qrisButton, new Color(150, 150, 150));
            }
        }
        System.out.println("Payment buttons updated. Cash active: " + paymentSettingsRepo.isMethodActive("TUNAI") + ", QRIS active: " + paymentSettingsRepo.isMethodActive("QRIS"));
    }


    private void handleOrderItemClick(int index) {
        if (index < 0 || index >= currentOrder.size()) {
            return;
        }

        OrderItem selectedOrderItem = currentOrder.get(index);

        String[] options = {"Kurangi Jumlah", "Hapus Item", "Batal"};
        int choice = JOptionPane.showOptionDialog(this,
                "Pilih aksi untuk '" + selectedOrderItem.getNama() + "' (jumlah: " + selectedOrderItem.getJumlah() + ")",
                "Aksi Item Order",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[2]);

        switch (choice) {
            case 0: // Kurangi Jumlah
                if (selectedOrderItem.getJumlah() > 1) {
                    selectedOrderItem.setJumlah(selectedOrderItem.getJumlah() - 1);
                    refreshOrderList();
                } else {
                    int confirmDelete = JOptionPane.showConfirmDialog(this,
                            "Jumlah sudah 1. Hapus item '" + selectedOrderItem.getNama() + "'?",
                            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirmDelete == JOptionPane.YES_OPTION) {
                        currentOrder.remove(index);
                        refreshOrderList();
                    }
                }
                break;
            case 1: // Hapus Item
                int confirmDelete = JOptionPane.showConfirmDialog(this,
                        "Hapus item '" + selectedOrderItem.getNama() + "' dari order?",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                if (confirmDelete == JOptionPane.YES_OPTION) {
                    currentOrder.remove(index);
                    refreshOrderList();
                }
                break;
            case 2: // Batal
                break;
        }
    }
}
