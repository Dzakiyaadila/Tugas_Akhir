//package View;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//
//public class panelMenu extends JPanel {
//    private DefaultTableModel tableModel;
//    private JTable table;
//    private String[] categories;
//    private String[] suppliers;
//
//    public panelMenu(String[] categories, String[] suppliers) {
//        this.categories = categories;
//        this.suppliers = suppliers;
//        setLayout(new BorderLayout(10, 10));
//
//        // Tabel
//        tableModel = new DefaultTableModel(new Object[]{"ID", "Nama", "Kategori", "Harga", "Supplier"}, 0);
//        table = new JTable(tableModel);
//        table.setRowHeight(30);
//
//        // Toolbar
//        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JButton addButton = new JButton("Tambah");
//        addButton.setBackground(new Color(76, 175, 80));
//        addButton.setForeground(Color.WHITE);
//        addButton.addActionListener(this::showAddDialog);
//
//        toolbar.add(addButton);
//
//        // Layout
//        add(new JScrollPane(table), BorderLayout.CENTER);
//        add(toolbar, BorderLayout.NORTH);
//
//        loadDummyData();
//    }
//
//    private void loadDummyData() {
//        tableModel.addRow(new Object[]{1, "Nasi Goreng", "Makanan", 25000, "Supplier A"});
//        tableModel.addRow(new Object[]{2, "Es Teh", "Minuman", 5000, "Supplier B"});
//    }
//
//    private void showAddDialog(ActionEvent e) {
//        JTextField nameField = new JTextField();
//        JComboBox<String> categoryCombo = new JComboBox<>(categories);
//        JTextField priceField = new JTextField();
//        JComboBox<String> supplierCombo = new JComboBox<>(suppliers);
//
//        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
//        panel.add(new JLabel("Nama Menu:"));
//        panel.add(nameField);
//        panel.add(new JLabel("Kategori:"));
//        panel.add(categoryCombo);
//        panel.add(new JLabel("Harga:"));
//        panel.add(priceField);
//        panel.add(new JLabel("Supplier:"));
//        panel.add(supplierCombo);
//
//        int result = JOptionPane.showConfirmDialog(
//                this, panel, "Tambah Menu",
//                JOptionPane.OK_CANCEL_OPTION
//        );
//
//        if (result == JOptionPane.OK_OPTION) {
//            tableModel.addRow(new Object[]{
//                    tableModel.getRowCount() + 1,
//                    nameField.getText(),
//                    categoryCombo.getSelectedItem(),
//                    Integer.parseInt(priceField.getText()),
//                    supplierCombo.getSelectedItem()
//            });
//        }
//    }
//}

package View;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
//import View.kategoriDialog;

public class panelMenu extends JPanel {
    private mainFrame mainFrame;
    private JTable menuTable;
    private DefaultTableModel tableModel;
    private CategorySelectionDialog kategoriDialog;
    private MenuFormDialog menuDialog;

    public panelMenu(mainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // 1. Header
        JLabel titleLabel = new JLabel("DAILY MENUS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 2. Tabel Menu
        String[] columns = {"NO", "NAMA MENU", "KATEGORI", "HARGA", "STOK", "SUPPLIER", "AKSI"};
        tableModel = new DefaultTableModel(columns, 0);
        menuTable = new JTable(tableModel);
        customizeTable();
        add(new JScrollPane(menuTable), BorderLayout.CENTER);

        // 3. Panel Aksi
        JPanel actionPanel = new JPanel(new BorderLayout());
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Cari");
        searchPanel.add(new JLabel("Search menu:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Add Button
        JButton addButton = new JButton("Tambah Menu");
        addButton.addActionListener(this::showAddMenuDialog);

        actionPanel.add(searchPanel, BorderLayout.WEST);
        actionPanel.add(addButton, BorderLayout.EAST);
        add(actionPanel, BorderLayout.SOUTH);

        // Load sample data
        loadSampleData();
    }

    private void customizeTable() {
        menuTable.setRowHeight(40);
        menuTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        menuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private void loadSampleData() {
        Object[][] data = {
                {1, "Nasi Goreng", "Makanan", 25000, 50, "Andi Kuniawan", "✏️ 🗑️"},
                {2, "Es Teh", "Minuman", 8000, 100, "Budi Raharja", "✏️ 🗑️"}
        };

        for (Object[] row : data) {
            tableModel.addRow(row);
        }
    }

    private void showAddMenuDialog(ActionEvent e) {
        // Buka dialog pilih kategori terlebih dahulu
        kategoriDialog categoryDialog = new kategoriDialog();
        categoryDialog.setVisible(true);

        if (categoryDialog.getSelectedCategory() != null) {
            // Lanjut ke form tambah menu
            menuDialog = new MenuFormDialog(kategoriDialog.getSelectedCategory());
            menuDialog.setVisible(true);

            if (menuDialog.isSubmitted()) {
                // Tambahkan ke tabel
                tableModel.addRow(new Object[]{
                        tableModel.getRowCount() + 1,
                        menuDialog.getMenuName(),
                        menuDialog.getCategory(),
                        menuDialog.getPrice(),
                        menuDialog.getStock(),
                        "Supplier A", // Ambil dari relasi kategori-supplier
                        "✏️ 🗑️"
                });
            }
        }
    }
    public class CategorySelectionDialog extends JDialog {
        private String selectedCategory;

        // Method yang diperlukan
        public String getSelectedCategory() {
            return selectedCategory;
        }
    }

    public class MenuFormDialog extends JDialog {
        private String category;
        private String menuName;
        private int price;
        private int stock;
        private boolean submitted = false;

        // Constructor dengan parameter kategori
        public MenuFormDialog(String category) {
            this.category = category;
            setTitle("Tambah Menu - Kategori: " + category);
            setSize(350, 250);
            setupForm();
        }

        private void setupForm() {
            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));

            JTextField nameField = new JTextField();
            JTextField priceField = new JTextField();
            JTextField stockField = new JTextField();

            panel.add(new JLabel("Nama Menu:"));
            panel.add(nameField);
            panel.add(new JLabel("Harga:"));
            panel.add(priceField);
            panel.add(new JLabel("Stok:"));
            panel.add(stockField);

            JButton submitBtn = new JButton("Simpan");
            submitBtn.addActionListener(e -> {
                this.menuName = nameField.getText();
                this.price = Integer.parseInt(priceField.getText());
                this.stock = Integer.parseInt(stockField.getText());
                this.submitted = true;
                dispose();
            });

            add(panel, BorderLayout.CENTER);
            add(submitBtn, BorderLayout.SOUTH);
        }

        // Getter methods
        public boolean isSubmitted() { return submitted; }
        public String getMenuName() { return menuName; }
        public String getCategory() { return category; }
        public int getPrice() { return price; }
        public int getStock() { return stock; }
    }
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new panelMenu().setVisible(true);
//        });
//    }
}