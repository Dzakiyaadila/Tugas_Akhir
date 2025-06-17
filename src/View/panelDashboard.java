package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelDashboard extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final mainFrame mainFrame;

    public panelDashboard(mainFrame mainFrame, CardLayout cardLayout, JPanel cardPanel) {
        this.mainFrame = mainFrame;
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;

        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // Tambahkan komponen utama
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createHomePanel(), BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Judul Aplikasi (kiri)
        JLabel titleLabel = new JLabel("CANTEEN MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Panel untuk navigasi + LOGOUT
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Glue untuk mendorong navigasi ke kanan (tambah di sini)
        navPanel.add(Box.createHorizontalGlue());

        // Tombol navigasi (HOME, CASHIER, REPORT)
        String[] navItems = {"HOME", "CASHIER", "REPORT"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            styleNavButton(navButton);
            navButton.addActionListener(e -> handleNavAction(item));
            navPanel.add(navButton);
            navPanel.add(Box.createHorizontalStrut(20)); // Jarak antar tombol
        }

        // Tombol LOGOUT (tetap di kanan)
        JButton logoutButton = new JButton("LOGOUT");
        styleLogoutButton(logoutButton);
        logoutButton.addActionListener(e -> mainFrame.logout());
        navPanel.add(Box.createHorizontalStrut(40)); // Jarak tambahan sebelum LOGOUT
        navPanel.add(logoutButton);

        headerPanel.add(navPanel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void styleNavButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 120, 215));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    private void styleLogoutButton(JButton button){
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.white);
        button.setBackground(new Color(200, 50, 50));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1), // Border putih
                BorderFactory.createEmptyBorder(5, 15, 5, 15) // Padding
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(true); // Aktifkan background
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        homePanel.setBackground(new Color(240, 240, 240));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        String[] titles = {"Supplier", "Category", "Menu", "Payment"};
        String[] icons = {"👥", "📦", "\uD83D\uDED2", "💳"};

        for (int i = 0; i < titles.length; i++) {
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            homePanel.add(createCrudCard(titles[i], icons[i]), gbc);
        }

        return homePanel;
    }

    private JPanel createCrudCard(String title, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(Color.WHITE);
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        card.add(iconLabel, BorderLayout.CENTER);

        // Footer Panel
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);

        // Title
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        footerPanel.add(titleLabel, BorderLayout.CENTER);

        // Manage Button
        JButton actionButton = new JButton("Manage");
        styleManageButton(actionButton);
        actionButton.addActionListener(e -> handleCrudAction(title));

        footerPanel.add(actionButton, BorderLayout.SOUTH);
        card.add(footerPanel, BorderLayout.SOUTH);

        return card;
    }

    private void styleManageButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
//INI NANTI KALAU UDAH ADA LOGIC NYA LANGSUNG DIIMPLEMENTASIKAN KE SINI YAAA!!!
    private void handleNavAction(String navItem) {
        switch(navItem) {
            case "HOME":
                cardLayout.show(cardPanel, "DASHBOARD");
                break;
            case "CASHIER":
                cardLayout.show(cardPanel, "KASIR");

                break;
            case "REPORT":
                JOptionPane.showMessageDialog(this, "Report module will be implemented");
                break;
        }
    }

    private void handleCrudAction(String title) {
        switch(title) {
            case "Supplier":
                mainFrame.showPanel("SUPPLIER");
                break;
            case "Category":
                mainFrame.showPanel("CATEGORY");
                break;
            case "Menu":
                mainFrame.showPanel("MENU");
                break;
            case "Payment":
                mainFrame.showPanel("PAYMENT");
                break;
        }
    }
}

