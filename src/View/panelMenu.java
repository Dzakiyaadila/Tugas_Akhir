package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.util.stream.Collectors;
import java.util.List;
import Logic.menu;
import Logic.kategori;
import Logic.supplier;
import CRUD.repoMenu;
import CRUD.repoKategori;
import CRUD.repoSupplier;

public class panelMenu extends JPanel {
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private final mainFrame mainAppFrame;
    private repoMenu menuRepo;
    private repoKategori kategoriRepo;
    private repoSupplier supplierRepo;

    public panelMenu(mainFrame mainAppFrame, repoMenu menuRepo, repoKategori kategoriRepo, repoSupplier supplierRepo) {
        this.mainAppFrame = mainAppFrame;
        this.menuRepo = menuRepo;
        this.kategoriRepo = kategoriRepo;
        this.supplierRepo = supplierRepo;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createHeaderPanel(), BorderLayout.NORTH);
        setupMenuTable();
        add(createActionPanel(), BorderLayout.SOUTH);
        refreshMenuData();
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
        String[] columns = {"NO", "ID", "NAMA MENU", "KATEGORI", "HARGA", "STOK", "SUPPLIER", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return column == 7;
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
    }

    public void refreshMenuData() {
        tableModel.setRowCount(0);
        List<menu> allMenus = menuRepo.getListMenu();

        int no = 1;
        for (menu m : allMenus) {
            tableModel.addRow(new Object[]{
                    no++,
                    m.getId(),
                    m.getNama(),
                    m.getKategori().getNama(),
                    m.getHarga(),
                    m.getStok(),
                    m.getSupplier(),
                    "✏️ 🗑️"
            });
        }
    }

    private void addActionButtonsToTable() {
        menuTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        menuTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
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
        List<kategori> categories = kategoriRepo.getCategories();
        String[] categoryNames = categories.stream().map(k -> k.getNama()).toArray(String[]::new);
        JComboBox<String> categoryCombo = new JComboBox<>(categoryNames);
        JTextField priceField = new JTextField();
        JTextField stockField = new JTextField();
        List<supplier> suppliers = supplierRepo.getSuppliers(); // ambil daftar supplier dari repoSupplier
        String[] supplierNames = suppliers.stream()
                .map(s -> s.getNama())
                .toArray(String[]::new);
        JComboBox<String> supplierCombo = new JComboBox<>(supplierNames);

        formPanel.add(new JLabel("Nama Menu:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Kategori:")); formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Harga (Rp):")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stok:")); formPanel.add(stockField);
        formPanel.add(new JLabel("Supplier:")); formPanel.add(supplierCombo);

        JButton submitButton = new JButton("Simpan");
        submitButton.addActionListener(ev -> {
            try {
                String namaMenu = nameField.getText();
                String selectedCategoryName = (String) categoryCombo.getSelectedItem();
                double hargaMenu = Double.parseDouble(priceField.getText());
                int stokMenu = Integer.parseInt(stockField.getText());
                String selectedSupplierName = (String) supplierCombo.getSelectedItem();

                kategori selectedKategori = kategoriRepo.getCategories().stream()
                        .filter(k -> k.getNama().equals(selectedCategoryName))
                        .findFirst()
                        .orElse(null);

                if (selectedKategori != null) {
                    menuRepo.addMenu(namaMenu, hargaMenu, selectedKategori, stokMenu, selectedSupplierName);
                    refreshMenuData();
                    dialog.dispose();
                    // meminta mainframe refresh panelKasir
                    mainAppFrame.getKasirPanel().refreshMenuPanel();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Kategori tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }

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
        int menuId = (int) tableModel.getValueAt(row, 1);
        menu existingMenu = menuRepo.getMenuById(menuId);

        if (existingMenu == null) {
            JOptionPane.showMessageDialog(this, "Menu tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog();
        dialog.setTitle("Edit Menu");
        dialog.setSize(400, 350);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JTextField nameField = new JTextField(existingMenu.getNama());
        List<kategori> categories = kategoriRepo.getCategories();
        String[] categoryNames = categories.stream().map(k -> k.getNama()).toArray(String[]::new);
        JComboBox<String> categoryCombo = new JComboBox<>(categoryNames);
        categoryCombo.setSelectedItem(existingMenu.getKategori().getNama());
        JTextField priceField = new JTextField(String.valueOf(existingMenu.getHarga()));
        JTextField stockField = new JTextField(String.valueOf(existingMenu.getStok()));
        List<supplier> suppliers = supplierRepo.getSuppliers(); // ambil daftar supplier
        String[] supplierNames = suppliers.stream()
                .map(s -> s.getNama())
                .toArray(String[]::new);
        JComboBox<String> supplierCombo = new JComboBox<>(supplierNames); // buat JComboBox
        supplierCombo.setSelectedItem(existingMenu.getSupplier());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.add(new JLabel("Nama Menu:")); formPanel.add(nameField);
        formPanel.add(new JLabel("Kategori:")); formPanel.add(categoryCombo);
        formPanel.add(new JLabel("Harga (Rp):")); formPanel.add(priceField);
        formPanel.add(new JLabel("Stok:")); formPanel.add(stockField);
        formPanel.add(new JLabel("Supplier:")); formPanel.add(supplierCombo);

        JButton saveButton = new JButton("Simpan Perubahan");
        saveButton.addActionListener(ev -> {
            try {
                String newNama = nameField.getText();
                String newCategoryName = (String) categoryCombo.getSelectedItem();
                double newHarga = Double.parseDouble(priceField.getText());
                int newStok = Integer.parseInt(stockField.getText());
                String newSupplier = (String) supplierCombo.getSelectedItem();

                kategori newKategori = kategoriRepo.getCategories().stream()
                        .filter(k -> k.getNama().equals(newCategoryName))
                        .findFirst()
                        .orElse(null);

                if (newKategori != null) {
                    menuRepo.updateMenu(menuId, newNama, newHarga, newKategori, newStok, newSupplier);
                    refreshMenuData();
                    dialog.dispose();
                    // meminta mainframe refresh panelKasir
                    mainAppFrame.getKasirPanel().refreshMenuPanel();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Kategori tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Harga dan Stok harus berupa angka.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void deleteMenu(int row) {
        int menuId = (int) tableModel.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus menu " + tableModel.getValueAt(row, 2) + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = menuRepo.deleteMenu(menuId);
            if (deleted) {
                refreshMenuData();
                // meminta mainframe refresh panelKasir
                mainAppFrame.getKasirPanel().refreshMenuPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus menu.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshMenuSupplierDropdown() {
        // dipake buat debugging aja
        System.out.println("PanelMenu: refreshMenuSupplierDropdown() dipanggil. " +
                "Dropdown supplier akan diperbarui saat dialog Add/Edit Menu dibuka.");
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1000, 600);
    }
}