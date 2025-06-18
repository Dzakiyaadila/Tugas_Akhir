package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import CRUD.repoKategori;
import Logic.kategori;


public class panelKategori extends JPanel implements KategoriDataChangeListener{
    private JTable table;
    private DefaultTableModel model;
    private final mainFrame mainAppFrame;
    private repoKategori kategoriRepo; // Tambahkan ini

    public panelKategori(mainFrame mainAppFrame, repoKategori kategoriRepo) {
        this.mainAppFrame = mainAppFrame;
        this.kategoriRepo = kategoriRepo;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER); // Main panel akan setup tabel
        //add(createFooterPanel(), BorderLayout.SOUTH);

        refreshCategoryData(); // Muat data kategori saat inisialisasi
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(0, 120, 215));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> mainAppFrame.showPanel("DASHBOARD"));
        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Categories", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JButton addButton = new JButton("+ Add Category");
        addButton.setFont(new Font("Arial", Font.PLAIN, 14));
        addButton.setBackground(new Color(0, 180, 120));
        addButton.setForeground(Color.WHITE);
        addButton.setBorderPainted(false);
        addButton.addActionListener(this::showAddCategoryDialog);
        headerPanel.add(addButton, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Table Setup
        String[] columnNames = {"ID", "KATEGORI", "AKSI"}; // Hapus kolom SUPPLIER sementara
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Hanya kolom aksi yang editable
            }
        };

        table = new JTable(model);
        customizeTable();

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return mainPanel;
    }

    private void customizeTable() {
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Sesuaikan lebar kolom aksi

        table.getColumn("AKSI").setCellRenderer(new ButtonRenderer());
        table.getColumn("AKSI").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void showAddCategoryDialog(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Category");
        dialog.setSize(400, 200);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Category Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(ev -> {
            String categoryName = nameField.getText().trim();
            if (categoryName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nama kategori tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            kategoriRepo.addkategori(categoryName);
            refreshCategoryData();
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Metode untuk memuat dan menampilkan data kategori dari repo
    public void refreshCategoryData() {
        model.setRowCount(0);
        for (kategori k : kategoriRepo.getCategories()) {
            model.addRow(new Object[]{k.getId(), k.getNama(), "Edit/Hapus"});
        }
    }

    // Custom Button Renderer (tetap sama)
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

    // Custom Button Editor (logika diperbarui untuk memanggil repoKategori)
    private class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

            editButton = new JButton("✏️");
            deleteButton = new JButton("🗑️");

            editButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    editCategory(row);
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    deleteCategory(row);
                }
                fireEditingStopped();
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return panel;
        }

        public Object getCellEditorValue() {
            return "Edit/Hapus";
        }
    }

    private void editCategory(int row) {
        int kategoriId = (int) model.getValueAt(row, 0); // Ambil ID dari tabel
        String currentName = (String) model.getValueAt(row, 1);

        String newName = JOptionPane.showInputDialog(this,
                "Edit nama kategori:", "Edit Category", JOptionPane.PLAIN_MESSAGE, null, null, currentName).toString();

        if (newName != null && !newName.trim().isEmpty()) {
            boolean updated = kategoriRepo.updateKategori(kategoriId, newName.trim());
            if (updated) {
                refreshCategoryData(); // Refresh tampilan tabel
                JOptionPane.showMessageDialog(this, "Kategori berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui kategori.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteCategory(int row) {
        int kategoriId = (int) model.getValueAt(row, 0);
        String categoryName = (String) model.getValueAt(row, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus kategori " + categoryName + ", semua supplier yang menyuplai kategori ini, dan semua menu yang terkait?", // Klarifikasi pesan
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean deleted = kategoriRepo.deleteKategori(kategoriId);
            if (deleted) {
                JOptionPane.showMessageDialog(this, "Kategori berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                // --- PENTING: Refresh semua panel yang terpengaruh ---
                mainAppFrame.getMenuPanel().refreshMenuData(); // Menu mungkin berubah karena kategori atau supplier dihapus
                mainAppFrame.getKasirPanel().refreshMenuPanel(); // Kasir juga berubah
                mainAppFrame.getSupplierPanel().refreshSupplierData(); // <-- TAMBAHKAN INI: Supplier juga berubah
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kategori.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void onKategoriDataChanged(List<kategori> updatedCategories) {
        System.out.println("PanelKategori: onKategoriDataChanged() dipanggil. Memperbarui tampilan...");
        refreshCategoryData(); // Panggil metode refresh yang sudah ada
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}