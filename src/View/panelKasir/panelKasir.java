package View.panelKasir;

import Logic.MenuItem;
import Logic.MenuUtil;
import Logic.transaksiDetail;
import View.mainFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class panelKasir extends JPanel {
    private JPanel menuPanel;
    private JPanel orderPanel;
    private DefaultListModel<String> orderListModel;
    private JLabel totalLabel;
    private java.util.List<OrderItem> currentOrder;

    public panelKasir() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        menuPanel = createMenuPanel();
        orderPanel = createOrderPanel();

        JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, orderPanel);
        contentPanel.setResizeWeight(0.7);
        contentPanel.setDividerSize(10);
        contentPanel.setContinuousLayout(true);
        contentPanel.setBorder(null);
        add(contentPanel, BorderLayout.CENTER);

        add(createFooterPanel(), BorderLayout.SOUTH);

        currentOrder = new ArrayList<>();
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("← Kembali");
        styleButton(backButton, new Color(200, 200, 200), Color.BLACK);
        backButton.addActionListener(e -> ((mainFrame) SwingUtilities.getWindowAncestor(this)).showDashboard());
        panel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("KASIR", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        return panel;
    }

    // ⬇️ SUDAH DIUBAH AGAR MENGAMBIL DARI menu_data.csv
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Menu Tersedia"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        List<MenuItem> allMenus = MenuUtil.loadMenuFromCSV("menu_data.csv");

        Map<String, JPanel> categoryPanels = new LinkedHashMap<>();
        categoryPanels.put("All Items", new JPanel(new GridLayout(0, 3, 10, 10)));

        for (MenuItem menu : allMenus) {
            categoryPanels.putIfAbsent(menu.getKategori(), new JPanel(new GridLayout(0, 3, 10, 10)));

            // buat panel untuk kategori spesifik
            JPanel itemPanelForCategory = createMenuItemPanel(menu);
            categoryPanels.get(menu.getKategori()).add(itemPanelForCategory);

            // buat panel untuk "All Items" juga (harus panel baru, bukan reuse yang tadi)
            JPanel itemPanelForAll = createMenuItemPanel(menu);
            categoryPanels.get("All Items").add(itemPanelForAll);
        }



        for (Map.Entry<String, JPanel> entry : categoryPanels.entrySet()) {
            JScrollPane scrollPane = new JScrollPane(entry.getValue());
            entry.getValue().setBorder(new EmptyBorder(10, 10, 10, 10));
            tabbedPane.addTab(entry.getKey(), scrollPane);
        }

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMenuItemPanel(MenuItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel nameLabel = new JLabel(item.getNama());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.add(nameLabel, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        JLabel priceLabel = new JLabel("Harga: Rp " + item.getHarga());
        JLabel stockLabel = new JLabel("Stok: " + item.getStok() + " unit");
        infoPanel.add(priceLabel);
        infoPanel.add(stockLabel);
        panel.add(infoPanel, BorderLayout.CENTER);

        JButton addButton = new JButton("+ Tambah");
        styleButton(addButton, new Color(0, 180, 120), Color.WHITE);
        addButton.addActionListener(e -> {
            addToOrder(new String[]{
                    item.getNama(),
                    String.valueOf(item.getHarga()),
                    String.valueOf(item.getStok())
            });
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

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton cashButton = new JButton("Tunai");
        stylePaymentButton(cashButton, new Color(0, 180, 60));
        cashButton.addActionListener(e -> processPayment("CASH"));

        JButton qrisButton = new JButton("QRIS");
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

    private void addToOrder(String[] itemData) {
        String itemName = itemData[0];
        int harga = Integer.parseInt(itemData[1]);
        int stok = Integer.parseInt(itemData[2]);

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
        int total = 0;
        for (OrderItem oi : currentOrder) {
            orderListModel.addElement(oi.getNama() + " x" + oi.getJumlah() + " - Rp " + (oi.getHarga() * oi.getJumlah()));
            total += oi.getHarga() * oi.getJumlah();
        }
        totalLabel.setText("Total: Rp " + total);
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

        int total = currentOrder.stream().mapToInt(oi -> oi.getHarga() * oi.getJumlah()).sum();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Total: Rp " + total +
                        "\nMetode: " + paymentMethod +
                        "\nSimpan transaksi?",
                "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            saveTransaction(paymentMethod);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            resetOrder();
        }
    }

    private void saveTransaction(String metode) {
        String waktu = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        int total = currentOrder.stream().mapToInt(oi -> oi.getHarga() * oi.getJumlah()).sum();

        List<transaksiDetail> detailList = new ArrayList<>();
        for (OrderItem oi : currentOrder) {
            detailList.add(new transaksiDetail(oi.getNama(), oi.getJumlah(), oi.getHarga() * oi.getJumlah()));
        }

        int userId = 1; // kamu bisa ganti ini sesuai sesi login
        Logic.transaksi trx = new Logic.transaksi(userId, (double) total, metode, detailList);

        Logic.CSVUtil.simpanTransaksi(trx);
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

    public void refreshMenuPanel() {
        remove(menuPanel); // hapus panel lama
        menuPanel = createMenuPanel(); // buat ulang panel baru

        // ambil SplitPane dan ganti komponen kiri-nya
        Component comp = getComponent(1);
        if (comp instanceof JSplitPane splitPane) {
            splitPane.setLeftComponent(menuPanel);
        }

        revalidate();
        repaint();
    }


}
