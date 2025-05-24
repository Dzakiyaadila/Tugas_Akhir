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

        // Judul Aplikasi
        JLabel titleLabel = new JLabel("CANTEEN MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Menu Navigasi
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);

        String[] navItems = {"HOME", "CASHIER", "REPORT"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            styleNavButton(navButton);

            navButton.addActionListener(e -> handleNavAction(item));
            navPanel.add(navButton);
        }

        // Logout Button
        JButton logoutButton = new JButton("LOGOUT");
        styleNavButton(logoutButton);
        logoutButton.addActionListener(e -> mainFrame.logout());

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.add(navPanel, BorderLayout.CENTER);
        rightPanel.add(logoutButton, BorderLayout.EAST);

        headerPanel.add(rightPanel, BorderLayout.CENTER);
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

