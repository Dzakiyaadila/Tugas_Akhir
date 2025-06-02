package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class panelMenu extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private final String csvFilePath = "menu_data.csv"; // File CSV
    private final mainFrame mainAppFrame;

    public panelMenu(mainFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createHeaderPanel(), BorderLayout.NORTH);
        setupMenuTable();
        add(createActionPanel(), BorderLayout.SOUTH);
//        loadMenuFromCSV();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        JLabel titleLabel = new JLabel("DAILY MENUS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        backButton.setBackground(new Color(0, 120, 215));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> mainAppFrame.showPanel("DASHBOARD"));
        headerPanel.add(backButton, BorderLayout.WEST);

        return headerPanel;
    }

    private void setupMenuTable() {
        String[] columns = {"NO", "NAMA MENU", "KATEGORI", "HARGA", "STOK", "SUPPLIER", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        menuTable = new JTable(tableModel);
        menuTable.setRowHeight(40);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader header = menuTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        addActionButtonsToTable();

        loadMenuFromCSV();
    }

    private void loadMenuFromCSV() {
        tableModel.setRowCount(0);
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            int no = 1;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 5) {
                    tableModel.addRow(new Object[]{
                            no++, data[0], data[1], Integer.parseInt(data[2]),
                            Integer.parseInt(data[3]), data[4], "✏️ 🗑️"
                    });
                }
            }
        } catch (IOException e) {
            System.err.println("Gagal membaca file CSV: " + e.getMessage());
        }
    }
    private void saveTableToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String line = tableModel.getValueAt(i, 1) + ";" +  // Nama Menu
                        tableModel.getValueAt(i, 2) + ";" +  // Kategori
                        tableModel.getValueAt(i, 3) + ";" +  // Harga
                        tableModel.getValueAt(i, 4) + ";" +  // Stok
                        tableModel.getValueAt(i, 5);         // Supplier
                writer.write(line);
                writer.newLine(); // Ganti baris
            }
            writer.flush(); // pastikan ditulis ke file sebelum refresh

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data ke file CSV:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return; // keluar dari fungsi supaya tidak coba refresh
        }

        // Refresh panelKasir setelah data berhasil disimpan
        mainFrame frame = (mainFrame) SwingUtilities.getWindowAncestor(this);
        if (frame.getKasirPanel() != null) {
            frame.getKasirPanel().refreshMenuPanel();
        }
    }





    private void addActionButtonsToTable() {
        menuTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setBackground(new Color(240, 240, 240));

        JButton addButton = new JButton("Tambah Menu");
        styleAddButton(addButton);

        panel.add(addButton, BorderLayout.EAST);
        return panel;
    }

    private void styleAddButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 180, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(this::showAddMenuDialog);
    }

    private void showAddMenuDialog(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Menu Baru");
        dialog.setSize(400, 350);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Makanan", "Minuman"});
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        JTextField supplierField = new JTextField();

        formPanel.add(new JLabel("Nama Menu:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Kategori:")); formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Harga (Rp):")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stok:")); formPanel.add(stockField);
        formPanel.add(new JLabel("Supplier:")); formPanel.add(supplierField);

        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(ev -> {
            try {
                Object[] row = {
                        tableModel.getRowCount() + 1,
                        nameField.getText(),
                        categoryCombo.getSelectedItem(),
                        Integer.parseInt(priceField.getText()),
                        Integer.parseInt(stockField.getText()),
                        supplierField.getText(),
                        "✏️ 🗑️"
                };
                tableModel.addRow(row);
                saveTableToCSV();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga dan Stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value == null ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        private final JPanel panel;
        private final JButton editButton, deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("✏️");
            deleteButton = new JButton("🗑️");

            editButton.addActionListener(e -> {
                int row = menuTable.getSelectedRow();
                if (row != -1) editMenu(row);
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = menuTable.getSelectedRow();
                if (row != -1) deleteMenu(row);
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }

    private void editMenu(int row) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Menu");
        dialog.setSize(400, 350);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField(tableModel.getValueAt(row, 1).toString());
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Makanan", "Minuman"});
        categoryCombo.setSelectedItem(tableModel.getValueAt(row, 2));
        JTextField priceField = new JTextField(tableModel.getValueAt(row, 3).toString());
        JTextField stockField = new JTextField(tableModel.getValueAt(row, 4).toString());
        JTextField supplierField = new JTextField(tableModel.getValueAt(row, 5).toString());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(new JLabel("Nama Menu:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Kategori:")); formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Harga (Rp):")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stok:")); formPanel.add(stockField);
        formPanel.add(new JLabel("Supplier:")); formPanel.add(supplierField);

        JButton saveButton = new JButton("Simpan Perubahan");
        saveButton.addActionListener(ev -> {
            try {
                tableModel.setValueAt(nameField.getText(), row, 1);
                tableModel.setValueAt(categoryCombo.getSelectedItem(), row, 2);
                tableModel.setValueAt(Integer.parseInt(priceField.getText()), row, 3);
                tableModel.setValueAt(Integer.parseInt(stockField.getText()), row, 4);
                tableModel.setValueAt(supplierField.getText(), row, 5);
                saveTableToCSV();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga dan Stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteMenu(int row) {
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus menu " + tableModel.getValueAt(row, 1) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
            saveTableToCSV();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600);
    }
}
