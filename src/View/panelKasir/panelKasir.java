package View.panelKasir;
import View.mainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.border.EmptyBorder;

public class panelKasir extends JPanel {
    private JPanel menuPanel;
    private JPanel orderPanel;
    private DefaultListModel<String> orderListModel;
    private JLabel totalLabel;

    public panelKasir() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // 2. Main Content (Menu + Order) dengan JSplitPane
        menuPanel = createMenuPanel();
        orderPanel = createOrderPanel();

        JSplitPane contentPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuPanel, orderPanel);
        contentPanel.setResizeWeight(0.7); // 70% untuk menu, 30% untuk order
        contentPanel.setDividerSize(10);
        contentPanel.setContinuousLayout(true);
        contentPanel.setBorder(null);
        add(contentPanel, BorderLayout.CENTER);

        // 3. Footer Panel
        add(createFooterPanel(), BorderLayout.SOUTH);
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

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Menu Tersedia"));

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));

        String[] categories = {"All Items", "Food", "Drink", "Snack"};
        int[] itemCounts = {254, 142, 36, 76};

        for (int i = 0; i < categories.length; i++) {
            JPanel categoryPanel = new JPanel(new GridLayout(0, 3, 10, 10));
            categoryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            String[][] sampleItems = {
                    {"PENSIL", "2,000", "100"},
                    {"Aqua", "12,000", "10"},
                    {"Sosis", "8,000", "9"},
                    {"Chocolatos", "1,500", "20"},
                    {"Susu", "8,800", "5"}
            };

            for (String[] item : sampleItems) {
                JPanel itemPanel = createMenuItemPanel(item);
                categoryPanel.add(itemPanel);
            }

            JScrollPane scrollPane = new JScrollPane(categoryPanel);
            tabbedPane.addTab(categories[i] + " (" + itemCounts[i] + ")", scrollPane);
        }

        panel.add(tabbedPane, BorderLayout.CENTER);
        return panel;
    }
//
//    private JPanel createMenuItemPanel(String[] itemData) {
//        JPanel panel = new JPanel(new BorderLayout());
//        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
//        panel.setBackground(Color.WHITE);
//        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//
//        JLabel nameLabel = new JLabel(itemData[0]);
//        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
//        nameLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
//        panel.add(nameLabel, BorderLayout.NORTH);
//
//        JPanel pricePanel = new JPanel(new GridLayout(0, 1));
//        for (int i = 1; i < itemData.length; i++) {
//            JLabel priceLabel = new JLabel("Rp " + itemData[i]);
//            priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
//            priceLabel.setBorder(new EmptyBorder(0, 10, 5, 10));
//            pricePanel.add(priceLabel);
//        }
//        JLabel stockLabel = new JLabel("Stok: " + itemData[2] + "unit");
//        JButton addButton = new JButton("+ Tambah");
//        styleButton(addButton, new Color(0, 180, 120), Color.WHITE);
//        addButton.addActionListener(e -> addToOrder(itemData));
//
//        panel.add(pricePanel, BorderLayout.CENTER);
//        panel.add(addButton, BorderLayout.SOUTH);
//
//        return panel;
//    }

    private JPanel createMenuItemPanel(String[] itemData) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        panel.setBackground(Color.WHITE);
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Nama Produk
        JLabel nameLabel = new JLabel(itemData[0]);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setBorder(new EmptyBorder(10, 10, 5, 10));
        panel.add(nameLabel, BorderLayout.NORTH);

        // Harga dan Stok
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));

        JLabel priceLabel = new JLabel("Harga: Rp " + itemData[1]);
        priceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        priceLabel.setBorder(new EmptyBorder(0, 10, 5, 10));
        infoPanel.add(priceLabel);

        JLabel stockLabel = new JLabel("Stok: " + itemData[2] + " unit");
        stockLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        stockLabel.setBorder(new EmptyBorder(0, 10, 5, 10));
        infoPanel.add(stockLabel);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Tombol Tambah
        JButton addButton = new JButton("+ Tambah");
        styleButton(addButton, new Color(0, 180, 120), Color.WHITE);
        addButton.addActionListener(e -> addToOrder(itemData));

        panel.add(addButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Order Aktif"));

        orderListModel = new DefaultListModel<>();
        JList<String> orderList = new JList<>(orderListModel);
        orderList.setCellRenderer(new OrderListRenderer());
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);

        // Panel Total + Tombol Bayar
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        totalLabel = new JLabel("Total: Rp 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
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
        orderListModel.addElement(itemData[0] + " @ Rp " + itemData[1]);
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < orderListModel.size(); i++) {
            String item = orderListModel.get(i);
            String priceStr = item.split("Rp ")[1];
            total += Double.parseDouble(priceStr.replace(",", ""));
        }
        totalLabel.setText("Total: Rp " + String.format("%,.0f", total));
    }

    private void resetOrder() {
        orderListModel.clear();
        updateTotal();
    }

    private void processPayment(String paymentMethod) {
        if (orderListModel.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada item dalam order!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double total = calculateTotal();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Total: Rp " + String.format("%,.0f", total) +
                        "\nMetode: " + (paymentMethod.equals("CASH") ? "Tunai" : "QRIS") +
                        "\nKonfirmasi pembayaran?",
                "Konfirmasi Pembayaran", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (processPaymentLogic(total, paymentMethod)) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                resetOrder();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memproses pembayaran!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double calculateTotal() {
        double total = 0;
        for (int i = 0; i < orderListModel.size(); i++) {
            String item = orderListModel.get(i);
            String priceStr = item.split("Rp ")[1];
            total += Double.parseDouble(priceStr.replace(",", ""));
        }
        return total;
    }

    private boolean processPaymentLogic(double total, String paymentMethod) {
        return true; // Simulasi sukses
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

    private static class OrderListRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(new EmptyBorder(5, 10, 5, 10));
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            return label;
        }
    }
}

