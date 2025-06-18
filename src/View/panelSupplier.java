package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import CRUD.repoSupplier;
import Logic.supplier;
import CRUD.repoKategori;
import Logic.kategori;

public class panelSupplier extends JPanel {
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private mainFrame mainAppFrame;
    private repoSupplier supplierRepo;
    private repoKategori kategoriRepo;

    public panelSupplier(mainFrame mainAppFrame, repoSupplier supplierRepo, repoKategori kategoriRepo) {
        this.mainAppFrame = mainAppFrame;
        this.supplierRepo = supplierRepo;
        this.kategoriRepo = kategoriRepo;

        setLayout(new BorderLayout()); // Layout utama
        setBackground(new Color(240, 240, 240));

        // 1. Header Panel (dengan tombol Back dan Add)
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Main Content (Tabel Supplier)
        setupSupplierTable(); // Ini akan menambahkan JScrollPane ke BorderLayout.CENTER

        // 3. Muat data saat panel diinisialisasi
        refreshSupplierData(); // Ini harus dipanggil setelah tableModel dibuat
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Border yang konsisten

        // tombol Back
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(0, 120, 215));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            // Dapatkan parent frame (mainFrame)
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window instanceof mainFrame) {
                ((mainFrame) window).showDashboard();
            }
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        // Judul Panel
        JLabel titleLabel = new JLabel("MANAJEMEN SUPPLIER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // tombol Add Supplier (dipindahkan ke header, seperti di panelKategori)
        JButton addButton = new JButton("+ Add Supplier");
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.setBackground(new Color(0, 180, 120));
        addButton.setForeground(Color.WHITE);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.addActionListener(this::showAddSupplierDialog);
        headerPanel.add(addButton, BorderLayout.EAST);

        return headerPanel;
    }

    private void setupSupplierTable() {
        String[] columns = {"ID", "NAMA SUPPLIER", "TELEPON", "ALAMAT", "KATEGORI", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Hanya kolom aksi yang editable
            }
        };

        supplierTable = new JTable(tableModel);

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

        // Column Widths (opsional, bisa disesuaikan)
        supplierTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        supplierTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Nama
        supplierTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Telepon
        supplierTable.getColumnModel().getColumn(3).setPreferredWidth(200); // Alamat
        supplierTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Kategori
        supplierTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Aksi

        supplierTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        supplierTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(supplierTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER); // Tambahkan scrollPane ke CENTER
    }

    public void refreshSupplierData() {
        tableModel.setRowCount(0); // Kosongkan tabel
        for (supplier s : supplierRepo.getSuppliers()) {
            tableModel.addRow(new Object[]{
                    s.getId(),
                    s.getNama(),
                    s.getTelepon(),
                    s.getAlamat(),
                    s.getKategori() != null ? s.getKategori().getNama() : "N/A",
                    "Edit/Hapus"
            });
        }
    }


    private void showAddSupplierDialog(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Tambah Supplier Baru");
        dialog.setSize(400, 300);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        List<Logic.kategori> categories = kategoriRepo.getCategories();
        String[] categoryNames = categories.stream().map(k -> k.getNama()).toArray(String[]::new);
        JComboBox<String> kategoriCombo = new JComboBox<>(categoryNames);
        formPanel.add(kategoriCombo);

        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(ev -> {
            String nama = namaField.getText().trim();
            String telepon = telpField.getText().trim();
            String alamat = alamatField.getText().trim();
            String selectedCategoryName = (String) kategoriCombo.getSelectedItem();

            if (nama.isEmpty() || telepon.isEmpty() || alamat.isEmpty() || selectedCategoryName == null) {
                JOptionPane.showMessageDialog(dialog, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            kategori selectedKategori = kategoriRepo.getCategories().stream()
                    .filter(k -> k.getNama().equals(selectedCategoryName))
                    .findFirst()
                    .orElse(null);

            if (selectedKategori != null) {
                supplierRepo.addSupplier(nama, telepon, alamat, selectedKategori);
                refreshSupplierData();
                dialog.dispose();
                mainAppFrame.getMenuPanel().refreshMenuSupplierDropdown();
            } else {
                JOptionPane.showMessageDialog(dialog, "Kategori tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() { setOpaque(true); }
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString()); return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setBackground(Color.WHITE);

            JButton editBtn = new JButton("✏️");
            editBtn.addActionListener(e -> {
                editSupplier(row);
                fireEditingCanceled();
            });

            JButton deleteBtn = new JButton("🗑️");
            deleteBtn.addActionListener(e -> {
                deleteSupplier(row);
                fireEditingCanceled();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);
            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            // Kembalikan nilai asli, bukan nilai dari checkbox
            return "Edit/Hapus";
        }
    }

    private void editSupplier(int row) {
        int supplierId = (int) tableModel.getValueAt(row, 0);
        supplier existingSupplier = supplierRepo.getSupplierById(supplierId);

        if (existingSupplier == null) {
            JOptionPane.showMessageDialog(this, "Supplier tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Supplier");
        dialog.setSize(400, 300);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField namaField = new JTextField(existingSupplier.getNama());
        JTextField telpField = new JTextField(existingSupplier.getTelepon());
        JTextField alamatField = new JTextField(existingSupplier.getAlamat());

        formPanel.add(new JLabel("Nama Supplier:")); formPanel.add(namaField);
        formPanel.add(new JLabel("Telepon:")); formPanel.add(telpField);
        formPanel.add(new JLabel("Alamat:")); formPanel.add(alamatField);

        formPanel.add(new JLabel("Kategori:"));
        List<Logic.kategori> categories = kategoriRepo.getCategories();
        String[] categoryNames = categories.stream().map(k -> k.getNama()).toArray(String[]::new);
        JComboBox<String> kategoriCombo = new JComboBox<>(categoryNames);
        if (existingSupplier.getKategori() != null) {
            kategoriCombo.setSelectedItem(existingSupplier.getKategori().getNama());
        }
        formPanel.add(kategoriCombo);


        JButton saveButton = new JButton("Simpan Perubahan");
        saveButton.addActionListener(ev -> {
            String newNama = namaField.getText().trim();
            String newTelepon = telpField.getText().trim();
            String newAlamat = alamatField.getText().trim();
            String newSelectedCategoryName = (String) kategoriCombo.getSelectedItem();

            if (newNama.isEmpty() || newTelepon.isEmpty() || newAlamat.isEmpty() || newSelectedCategoryName == null) {
                JOptionPane.showMessageDialog(dialog, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            kategori newKategori = kategoriRepo.getCategories().stream()
                    .filter(k -> k.getNama().equals(newSelectedCategoryName))
                    .findFirst()
                    .orElse(null);

            if (newKategori != null) {
                boolean updated = supplierRepo.updateSupplier(supplierId, newNama, newTelepon, newAlamat, newKategori);
                if (updated) {
                    refreshSupplierData();
                    JOptionPane.showMessageDialog(this, "Supplier berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    mainAppFrame.getMenuPanel().refreshMenuSupplierDropdown();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal memperbarui supplier.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Kategori tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteSupplier(int row) {
        int supplierId = (int) tableModel.getValueAt(row, 0);
        String supplierName = (String) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus supplier " + supplierName + " dan semua menu yang disuplainya?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = supplierRepo.deleteSupplier(supplierId);
            if (deleted) {
                refreshSupplierData();
                JOptionPane.showMessageDialog(this, "Supplier berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                mainAppFrame.getMenuPanel().refreshMenuSupplierDropdown();
                mainAppFrame.getMenuPanel().refreshMenuData();
                mainAppFrame.getKasirPanel().refreshMenuPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600); // Ukuran default panel
    }
}