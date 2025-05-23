package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class panelSupplier extends JFrame {
    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public panelSupplier() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // 1. Judul Panel
        JLabel titleLabel = new JLabel("MANAJEMEN SUPPLIER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 120, 215));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Tabel Supplier
        String[] columns = {"NAMA SUPPLIER", "TELEPON", "ALAMAT", "KATEGORI", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Hanya kolom aksi yang editable
            }
        };

        supplierTable = new JTable(tableModel);
        customizeTableAppearance();

        // 3. Tombol Aksi
        addActionButtonsToTable();

        // 4. Contoh Data
        addSampleData();

        // 5. Panel Tambah Supplier
        add(createAddSupplierPanel(), BorderLayout.SOUTH);
    }

    private void customizeTableAppearance() {
        // Style header
        JTableHeader header = supplierTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        // Style table
        supplierTable.setRowHeight(40);
        supplierTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        supplierTable.setShowGrid(false);
        supplierTable.setIntercellSpacing(new Dimension(0, 0));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addActionButtonsToTable() {
        // Kolom aksi
        supplierTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        supplierTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void addSampleData() {
        Object[][] data = {
                {"Jan Rodi", "082111234567", "Jakarta", "MAKANAN", "Edit/Hapus"},
                {"Agun", "081309888877", "Bipacel", "MINUMAN","Edit/Hapus"},
                {"Faber Castell", "085612345678", "Surabaya", "ALAT TULIS","Edit/Hapus"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private JPanel createAddSupplierPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        panel.setBackground(new Color(240, 240, 240));

        JButton addButton = new JButton("Tambah Supplier");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(0, 180, 120));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        addButton.addActionListener(e -> {
            // Dialog tambah supplier
            showAddSupplierDialog();
        });

        panel.add(addButton);
        return panel;
    }

    private void showAddSupplierDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Supplier Baru");
        dialog.setSize(400, 300);
        dialog.setModal(true);

        // Implementasi form tambah supplier di sini
        // ... (bisa ditambahkan JTextField untuk nama, telp, alamat)

        dialog.setVisible(true);
    }

    // Custom renderer dan editor untuk tombol aksi
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

    private class ButtonEditor extends DefaultCellEditor {
        private String label;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();

            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);

            JButton editBtn = new JButton("✏️");
            editBtn.addActionListener(e -> {
                // Implementasi edit
                editSupplier(row);
                fireEditingStopped();
            });

            JButton deleteBtn = new JButton("🗑️");
            deleteBtn.addActionListener(e -> {
                // Implementasi hapus
                deleteSupplier(row);
                fireEditingStopped();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
            return panel;
        }

        public Object getCellEditorValue() {
            return label;
        }
    }

    private void editSupplier(int row) {
        // Implementasi edit supplier
        JOptionPane.showMessageDialog(this, "Edit supplier: " + tableModel.getValueAt(row, 0));
    }

    private void deleteSupplier(int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus supplier " + tableModel.getValueAt(row, 0) + "?",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new panelSupplier().setVisible(true);
        });
    }
}