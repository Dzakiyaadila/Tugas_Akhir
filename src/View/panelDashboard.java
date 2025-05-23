package View;
import javax.swing.*;
import java.awt.*;

public class panelDashboard extends JFrame{
    public panelDashboard(){
        setTitle("Admin Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createHomePanel(), BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("CANTEEN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        navPanel.setOpaque(false);

        String[] navItems = {"HOME", "CASHIER", "REPORT"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setFont(new Font("Arial", Font.PLAIN, 16));
            navButton.setForeground(Color.WHITE);
            navButton.setBackground(new Color(0, 120, 215));
            navButton.setBorderPainted(false);
            navButton.setFocusPainted(false);

            navPanel.add(navButton);
        }
        headerPanel.add(navPanel, BorderLayout.CENTER);
        return headerPanel;
    }
    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;

        String[] titles = {"Supplier", "Category", "Menu", "Payment"};
        String[] icons = {"👥", "📦", "\uD83D\uDED2", "💳"};
        for (int i = 0; i < titles.length; i++) {
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            gbc.weightx = 1.0;
            gbc.weighty = 1.0;

            JPanel card = createCrudCard(titles[i], icons[i]);
            homePanel.add(card, gbc);
        }

        return homePanel;
    }
    private JPanel createCrudCard(String title, String icon) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(300, 200));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        card.add(iconLabel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        bottomPanel.add(titleLabel, BorderLayout.NORTH);

        JButton actionButton = new JButton("Manage");
        actionButton.setFont(new Font("Arial", Font.PLAIN, 14));

        actionButton.addActionListener(e -> handleCrudAction(title));
        bottomPanel.add(actionButton, BorderLayout.SOUTH);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }
    private void handleCrudAction(String title) {
        switch (title) {
            case "Supplier":
                new panelSupplier().setVisible(true);
//                break;
            case "Category":

                new panelKategori().setVisible(true);
             case "Menu":
                 new panelMenu(mainFrame).setVisible(true);
//                 break;
            // case "Payment":
            //     new panelPayment().setVisible(true);
            //     break;
        }
    }

    // Uncomment ini kalau udah ada logic navigasi
//    private void handleNavSelection(ActionEvent e) {
//        String command = ((JButton)e.getSource()).getText();
//        switch (command) {
//            case "CASHIER":
//                new panelPayment().setVisible(true);
//                this.dispose();
//                break;
//            case "REPORT":
//                new ReportView().setVisible(true);
//                this.dispose();
//                break;
//        }
//    }
}
