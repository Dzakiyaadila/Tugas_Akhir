package View;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class panelKategori extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private final mainFrame mainAppFrame;

    public panelKategori(mainFrame mainAppFrame) {
        this.mainAppFrame = mainAppFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));

        // 1. Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Main Content
        add(createMainPanel(), BorderLayout.CENTER);

        // 3. Footer
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 120, 215));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Back Button
        JButton backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBackground(new Color(0, 120, 215));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.addActionListener(e -> mainAppFrame.showPanel("DASHBOARD"));
        headerPanel.add(backButton, BorderLayout.WEST);

        // Title
        JLabel titleLabel = new JLabel("Categories", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Add Button
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
        String[] columnNames = {"ID", "KATEGORI", "SUPPLIER", "AKSI"};
        Object[][] data = {
                {"1", "MAKANAN", "JAN RODI", "Edit/Hapus"},
                {"2", "MINUMAN", "AGUS", "Edit/Hapus"},
                {"3", "ALAT TULIS", "ADIT", "Edit/Hapus"}
        };

        model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(model);
        customizeTable();

        mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return mainPanel;
    }

    private void customizeTable() {
        // Table Styling
        table.setRowHeight(40);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setShowGrid(false);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(0, 120, 215));
        header.setForeground(Color.WHITE);

        // Column Widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        // Action Buttons
        table.getColumn("AKSI").setCellRenderer(new ButtonRenderer());
        table.getColumn("AKSI").setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Pagination (Example)
        JButton prevButton = new JButton("←");
        JButton nextButton = new JButton("→");
        JLabel pageLabel = new JLabel("Page 1");

        footerPanel.add(prevButton);
        footerPanel.add(pageLabel);
        footerPanel.add(nextButton);

        return footerPanel;
    }

    private void showAddCategoryDialog(ActionEvent e) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Category");
        dialog.setSize(400, 250);
        dialog.setModal(true);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form Components
        formPanel.add(new JLabel("Category Name:"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Supplier:"));
        JComboBox<String> supplierCombo = new JComboBox<>(new String[]{"JAN RODI", "AGUS", "ADIT"});
        formPanel.add(supplierCombo);

        // Submit Button
        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(ev -> {
            model.addRow(new Object[]{
                    model.getRowCount() + 1,
                    nameField.getText(),
                    supplierCombo.getSelectedItem(),
                    "Edit/Hapus"
            });
            dialog.dispose();
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(submitButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // Custom Button Renderer
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

    // Custom Button Editor
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
            return "";
        }
    }

    private void editCategory(int row) {
        JOptionPane.showMessageDialog(this,
                "Edit kategori: " + model.getValueAt(row, 1),
                "Edit Category",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteCategory(int row) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus kategori " + model.getValueAt(row, 1) + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}