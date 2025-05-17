package displayDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelMenu extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private String[] categories;
    private String[] suppliers;

    public panelMenu(String[] categories, String[] suppliers) {
        this.categories = categories;
        this.suppliers = suppliers;
        setLayout(new BorderLayout(10, 10));

        // Tabel
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Kategori", "Harga", "Supplier"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);

        // Toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Tambah");
        addButton.setBackground(new Color(76, 175, 80));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(this::showAddDialog);

        toolbar.add(addButton);

        // Layout
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(toolbar, BorderLayout.NORTH);

        loadDummyData();
    }

    private void loadDummyData() {
        tableModel.addRow(new Object[]{1, "Nasi Goreng", "Makanan", 25000, "Supplier A"});
        tableModel.addRow(new Object[]{2, "Es Teh", "Minuman", 5000, "Supplier B"});
    }

    private void showAddDialog(ActionEvent e) {
        JTextField nameField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(categories);
        JTextField priceField = new JTextField();
        JComboBox<String> supplierCombo = new JComboBox<>(suppliers);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.add(new JLabel("Nama Menu:"));
        panel.add(nameField);
        panel.add(new JLabel("Kategori:"));
        panel.add(categoryCombo);
        panel.add(new JLabel("Harga:"));
        panel.add(priceField);
        panel.add(new JLabel("Supplier:"));
        panel.add(supplierCombo);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Tambah Menu",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            tableModel.addRow(new Object[]{
                    tableModel.getRowCount() + 1,
                    nameField.getText(),
                    categoryCombo.getSelectedItem(),
                    Integer.parseInt(priceField.getText()),
                    supplierCombo.getSelectedItem()
            });
        }
    }
}