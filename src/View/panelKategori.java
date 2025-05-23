//package View;
//import javax.swing.*;
//import javax.swing.table.*;
//import java.awt.*;
//
//public class panelKategori extends JFrame {
//    JTable table;
//    DefaultTableModel model;
//
//    public panelKategori() {
//        setLayout(new BorderLayout());
////        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//
//        // Title
//        JLabel sectionLabel = new JLabel("Categories", SwingConstants.LEFT);
//        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
//
//        // Buttons atas
//        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        JButton backButton = new JButton("←");
//        JButton addButton = new JButton("+");
//        topPanel.add(backButton);
//        topPanel.add(addButton);
//
//        // Table
//        String[] columnNames = {"ID", "KATEGORI", "SUPPLIER","AKSI"};
//        Object[][] data = {
//                {"1", "MAKANAN", "JAN RODI", "Edit/hapus"},
//                {"2", "MINUMAN", "AGUS", "Edit/hapus"},
//                {"3", "ALAT TULIS", "ADIT", "Edit/hapus"}
//        };
//
//        model = new DefaultTableModel(data, columnNames) {
//            public boolean isCellEditable(int row, int column) {
//                return column == 3;
//            }
//        };
//
//        table = new JTable(model);
//        table.setRowHeight(40);
//        table.getColumnModel().getColumn(0).setPreferredWidth(50);
//        table.getColumnModel().getColumn(1).setPreferredWidth(200);
//        table.getColumnModel().getColumn(2).setPreferredWidth(100);
//        table.getColumnModel().getColumn(3).setPreferredWidth(200);
//        table.getColumn("ACTIONS").setCellRenderer(new ButtonRenderer());
//        table.getColumn("ACTIONS").setCellEditor(new ButtonEditor(new JCheckBox()));
//
//        JScrollPane scrollPane = new JScrollPane(table);
//
//        // Bottom pagination
//        JPanel bottomPanel = new JPanel();
//        JButton pageButton = new JButton("1");
//        bottomPanel.add(pageButton);
//
//        // Gabungkan semua
//        add(sectionLabel, BorderLayout.NORTH);
//        add(topPanel, BorderLayout.BEFORE_FIRST_LINE);
//        add(scrollPane, BorderLayout.CENTER);
//        add(bottomPanel, BorderLayout.SOUTH);
//    }
//
//    // Renderer untuk tombol
//    class ButtonRenderer extends JPanel implements TableCellRenderer {
//        JButton editButton = new JButton("✏️ Edit");
//        JButton deleteButton = new JButton("🗑️ Delete");
//
//        public ButtonRenderer() {
//            setLayout(new FlowLayout(FlowLayout.LEFT));
//            add(editButton);
//            add(deleteButton);
//        }
//
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
//                                                       boolean hasFocus, int row, int column) {
//            return this;
//        }
//    }
//
//    // Editor untuk tombol
//    class ButtonEditor extends DefaultCellEditor {
//        protected JPanel panel;
//        protected JButton editButton;
//        protected JButton deleteButton;
//
//        public ButtonEditor(JCheckBox checkBox) {
//            super(checkBox);
//            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//            editButton = new JButton("✏️ Edit");
//            deleteButton = new JButton("🗑️ Delete");
//            panel.add(editButton);
//            panel.add(deleteButton);
//
//            editButton.addActionListener(e -> {
//                JOptionPane.showMessageDialog(null, "Edit clicked on row " + table.getSelectedRow());
//            });
//
//            deleteButton.addActionListener(e -> {
//                int row = table.getSelectedRow();
//                if (row != -1) model.removeRow(row);
//            });
//        }
//
//        public Component getTableCellEditorComponent(JTable table, Object value,
//                                                     boolean isSelected, int row, int column) {
//            return panel;
//        }
//
//        public Object getCellEditorValue() {
//            return "";
//        }
//    }
//}


package View;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class panelKategori extends JFrame {
    JTable table;
    DefaultTableModel model;

    public panelKategori() {
        setLayout(new BorderLayout());
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Title
        JLabel sectionLabel = new JLabel("Categories", SwingConstants.LEFT);
        sectionLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Buttons atas
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton backButton = new JButton("←");
        JButton addButton = new JButton("+");
        topPanel.add(backButton);
        topPanel.add(addButton);

        // Table
        String[] columnNames = {"ID", "KATEGORI", "SUPPLIER", "AKSI"};
        Object[][] data = {
                {"1", "MAKANAN", "JAN RODI", "Edit/hapus"},
                {"2", "MINUMAN", "AGUS", "Edit/hapus"},
                {"3", "ALAT TULIS", "ADIT", "Edit/hapus"}
        };

        model = new DefaultTableModel(data, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        table = new JTable(model);
        table.setRowHeight(40);

        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Perbaikan: Gunakan "AKSI" bukan "ACTIONS"
        table.getColumn("AKSI").setCellRenderer(new ButtonRenderer());
        table.getColumn("AKSI").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Bottom pagination
        JPanel bottomPanel = new JPanel();
        JButton pageButton = new JButton("1");
        bottomPanel.add(pageButton);

        // Gabungkan semua
        add(sectionLabel, BorderLayout.NORTH);
        add(topPanel, BorderLayout.PAGE_START); // Ganti BEFORE_FIRST_LINE
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Renderer untuk tombol
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        JButton editButton = new JButton("✏️ Edit");
        JButton deleteButton = new JButton("🗑️ Delete");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            setOpaque(true);
            add(editButton);
            add(deleteButton);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    // Editor untuk tombol
    class ButtonEditor extends DefaultCellEditor {
        protected JPanel panel;
        protected JButton editButton;
        protected JButton deleteButton;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            editButton = new JButton("✏️ Edit");
            deleteButton = new JButton("🗑️ Delete");
            panel.add(editButton);
            panel.add(deleteButton);

            editButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    JOptionPane.showMessageDialog(panelKategori.this,
                            "Edit kategori: " + model.getValueAt(row, 1));
                }
                fireEditingStopped();
            });

            deleteButton.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row != -1) {
                    int confirm = JOptionPane.showConfirmDialog(panelKategori.this,
                            "Hapus kategori " + model.getValueAt(row, 1) + "?",
                            "Konfirmasi", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        model.removeRow(row);
                    }
                }
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return panel;
        }

        public Object getCellEditorValue() {
            return "";
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new panelKategori().setVisible(true);
        });
    }
}