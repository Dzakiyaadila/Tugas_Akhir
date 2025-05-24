package View;

import javax.swing.*;
import java.awt.*;

public class MenuFormDialog extends JDialog {
    private String category;
    private String menuName;
    private int price;
    private int stock;
    private boolean submitted = false;

    public MenuFormDialog(String category) {
        this.category = category;
        setTitle("Tambah Menu - " + category);
        setSize(350, 250);
        setModal(true);
        setLocationRelativeTo(null);
        setupForm();
    }

    private void setupForm() {
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();

        formPanel.add(new JLabel("Nama Menu:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Harga:"));
        formPanel.add(priceField);
        formPanel.add(new JLabel("Stok:"));
        formPanel.add(stockField);

        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(e -> {
            this.menuName = nameField.getText();
            try {
                this.price = Integer.parseInt(priceField.getText());
                this.stock = Integer.parseInt(stockField.getText());
                this.submitted = true;
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Harga dan stok harus angka!");
            }
        });

        setLayout(new BorderLayout());
        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    // Getter methods
    public boolean isSubmitted() { return submitted; }
    public String getMenuName() { return menuName; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
}