package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelSupplier extends JPanel {
    private JTable supplierTable;
    private DefaultTableModel tableModel;

    public panelSupplier() {
        // Set layout dan background
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(240, 240, 240));

        // Panel untuk header (back button + title)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. Tombol Back
        JButton backButton = new JButton("← Kembali");
        styleBackButton(backButton);
        headerPanel.add(backButton, BorderLayout.WEST);

        // 2. Judul Panel (tetap di tengah)
        JLabel titleLabel = new JLabel("MANAJEMEN SUPPLIER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 120, 215));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        add(headerPanel, BorderLayout.NORTH);

        // 3. Tabel Supplier
        setupSupplierTable();

        // 4. Tombol Tambah Supplier
        add(createAddButtonPanel(), BorderLayout.SOUTH);
    }

    private void styleBackButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(new Color(200, 200, 200));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addActionListener(e -> {
            // Dapatkan parent frame (mainFrame)
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof mainFrame) {
                ((mainFrame) window).showDashboard();
            }
        });
    }

    private void setupSupplierTable() {
        // Model tabel
        String[] columns = {"NAMA SUPPLIER", "TELEPON", "ALAMAT", "KATEGORI", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Hanya kolom aksi yang editable
            }
        };

        supplierTable = new JTable(tableModel);
        styleTable();

        // Tambahkan contoh data
        addSampleData();
    }

    private void styleTable() {
        // Style header
        JTableHeader header = supplierTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        // Style tabel
        supplierTable.setRowHeight(40);
        supplierTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        supplierTable.setShowGrid(false);
        supplierTable.setIntercellSpacing(new Dimension(0, 0));

        // Kolom aksi
        supplierTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        supplierTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addSampleData() {
        Object[][] data = {
                {"Jan Rodi", "082111234567", "Jakarta", "MAKANAN", "Edit/Hapus"},
                {"Agun", "081309888877", "Bipacel", "MINUMAN", "Edit/Hapus"},
                {"Faber Castell", "085612345678", "Surabaya", "ALAT TULIS", "Edit/Hapus"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private JPanel createAddButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 20));
        panel.setBackground(new Color(240, 240, 240));

        JButton addButton = new JButton("Tambah Supplier");
        styleAddButton(addButton);
        panel.add(addButton);

        return panel;
    }

    private void styleAddButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 180, 120));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        button.addActionListener(this::showAddSupplierDialog);
    }

    private void showAddSupplierDialog(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Supplier Baru");
        dialog.setSize(400, 300);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        // Form tambah supplier
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Komponen form
        formPanel.add(new JLabel("Nama Supplier:"));
        JTextField namaField = new JTextField();
        formPanel.add(namaField);

        formPanel.add(new JLabel("Telepon:"));
        JTextField telpField = new JTextField();
        formPanel.add(telpField);

        formPanel.add(new JLabel("Alamat:"));
        JTextField alamatField = new JTextField();
        formPanel.add(alamatField);

        formPanel.add(new JLabel("Kategori:"));
        JComboBox<String> kategoriCombo = new JComboBox<>(new String[]{"MAKANAN", "MINUMAN", "ALAT TULIS"});
        formPanel.add(kategoriCombo);

        // Tombol submit
        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(ev -> {
            // Simpan data ke tabel
            tableModel.addRow(new Object[]{
                    namaField.getText(),
                    telpField.getText(),
                    alamatField.getText(),
                    kategoriCombo.getSelectedItem(),
                    "Edit/Hapus"
            });
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Custom renderer dan editor untuk tombol aksi
    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);

            JButton editBtn = new JButton("✏️");
            editBtn.addActionListener(e -> {
                editSupplier(row);
                fireEditingStopped();
            });

            JButton deleteBtn = new JButton("🗑️");
            deleteBtn.addActionListener(e -> {
                deleteSupplier(row);
                fireEditingStopped();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
            return panel;
        }
    }

    private void editSupplier(int row) {
        JOptionPane.showMessageDialog(this,
                "Edit supplier: " + tableModel.getValueAt(row, 0),
                "Edit Supplier",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteSupplier(int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus supplier " + tableModel.getValueAt(row, 0) + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(row);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600); // Ukuran default panel
    }
}