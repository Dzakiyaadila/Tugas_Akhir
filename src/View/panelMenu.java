package View;

import View.mainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelMenu extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private final mainFrame mainAppFrame;  // Untuk navigasi

    public panelMenu(mainFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // 1. Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Table Panel
        setupMenuTable();

        // 3. Action Panel
        add(createActionPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));

        // Title
        JLabel titleLabel = new JLabel("DAILY MENUS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Back Button
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
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Hanya kolom AKSI editable
            }
        };

        menuTable = new JTable(tableModel);
        customizeTableAppearance();
        addActionButtonsToTable();
        addContohData();
    }

    private void customizeTableAppearance() {
        menuTable.setRowHeight(40);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuTable.setShowGrid(false);
        menuTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = menuTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(menuTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addActionButtonsToTable() {
        // Kolom Aksi
        menuTable.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void addContohData() {
        Object[][] data = {
                {1, "Nasi Goreng", "Makanan", 25000, 50, "Andi Kuniawan", "✏️ 🗑️"},
                {2, "Es Teh", "Minuman", 8000, 100, "Budi Raharja", "✏️ 🗑️"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setBackground(new Color(240, 240, 240));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Cari");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add Button
        JButton addButton = new JButton("Tambah Menu");
        styleAddButton(addButton);

        panel.add(searchPanel, BorderLayout.WEST);
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

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Components
        formPanel.add(new JLabel("Nama Menu:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Kategori:"));
        JComboBox<String> categoryCombo = new JComboBox<>(new String[]{"Makanan", "Minuman"});
        formPanel.add(categoryCombo);

        formPanel.add(new JLabel("Harga (Rp):"));
        JTextField priceField = new JTextField();
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stok:"));
        JTextField stockField = new JTextField();
        formPanel.add(stockField);

        // Submit Button
        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(ev -> {
            try {
                tableModel.addRow(new Object[]{
                        tableModel.getRowCount() + 1,
                        nameField.getText(),
                        categoryCombo.getSelectedItem(),
                        Integer.parseInt(priceField.getText()),
                        Integer.parseInt(stockField.getText()),
                        "Supplier Default",
                        "✏️ 🗑️"
                });
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga dan Stok harus angka!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Custom Renderer untuk Tombol Aksi
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom Editor untuk Tombol Aksi
    private class ButtonEditor extends DefaultCellEditor {
        private String label;
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("✏️");
            deleteButton = new JButton("🗑️");

            editButton.addActionListener(e -> {
                int row = menuTable.getSelectedRow();
                if (row != -1) {
                    editMenu(row);
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = menuTable.getSelectedRow();
                if (row != -1) {
                    deleteMenu(row);
                }
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            return panel;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    private void editMenu(int row) {
        // Implementasi edit menu
        JOptionPane.showMessageDialog(this,
                "Edit menu: " + tableModel.getValueAt(row, 1),
                "Edit Menu",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteMenu(int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus menu " + tableModel.getValueAt(row, 1) + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600);
    }
}