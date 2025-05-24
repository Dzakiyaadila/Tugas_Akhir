package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelPayment extends JPanel {

    public panelPayment() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel Header (Back Button + Title)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));

        // 1. Tombol Back
        JButton backButton = new JButton("← Kembali");
        styleBackButton(backButton);
        headerPanel.add(backButton, BorderLayout.WEST);

        // 2. Judul
        JLabel headerLabel = new JLabel("Canteen!", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(new Color(0, 120, 215));
        headerPanel.add(headerLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // 3. Payment Methods Panel (Horizontal)
        JPanel paymentPanel = new JPanel();
        paymentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0)); // Spasi horizontal 30px
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        paymentPanel.setBackground(new Color(240, 240, 240));

        // Tunai Card
        JPanel tunaiCard = createToggleCard("Tunai", "💵", true);
        paymentPanel.add(tunaiCard);

        // QRIS Card
        JPanel qrisCard = createToggleCard("QRIS", "📱", true);
        paymentPanel.add(qrisCard);

        // Panel pembungkus untuk membuat payment methods lebih ke tengah
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(new Color(240, 240, 240));
        centerWrapper.add(paymentPanel);

        add(centerWrapper, BorderLayout.CENTER);

        // 4. Bottom Panel (Total)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.add(new JLabel("Total Payments: 2"));

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleBackButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof mainFrame) {
                ((mainFrame) window).showDashboard();
            }
        });
    }

    private JPanel createToggleCard(String title, String emoji, boolean isActive) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(isActive ? new Color(50, 200, 50) : new Color(200, 50, 50), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 150));

        // Icon & Title
        JLabel emojiLabel = new JLabel(emoji, SwingConstants.CENTER);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(emojiLabel, BorderLayout.CENTER);
        centerPanel.add(titleLabel, BorderLayout.SOUTH);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Toggle Button
        JToggleButton toggleBtn = new JToggleButton(isActive ? "ACTIVE" : "INACTIVE");
        toggleBtn.setSelected(isActive);
        toggleBtn.setFont(new Font("Arial", Font.BOLD, 12));
        toggleBtn.setBackground(isActive ? new Color(50, 200, 50) : new Color(200, 50, 50));
        toggleBtn.setForeground(Color.WHITE);
        toggleBtn.setFocusPainted(false);
        toggleBtn.setBorderPainted(false);

        toggleBtn.addActionListener(e -> {
            boolean selected = toggleBtn.isSelected();
            toggleBtn.setText(selected ? "ACTIVE" : "INACTIVE");
            toggleBtn.setBackground(selected ? new Color(50, 200, 50) : new Color(200, 50, 50));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(selected ? new Color(50, 200, 50) : new Color(200, 50, 50), 2),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
            ));
        });

        // Gabungkan Komponen Card
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(toggleBtn, BorderLayout.SOUTH);

        return card;
    }
}