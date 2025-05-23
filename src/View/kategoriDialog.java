package View;
import javax.swing.*;
import java.awt.*;

public class kategoriDialog extends JDialog {
    private JList<String> categoryList;
    private String selectedCategory;

    public kategoriDialog() {
        setTitle("Pilih Kategori");
        setSize(300, 400);
        setModal(true);

        // Data dummy kategori
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Makanan");
        listModel.addElement("Minuman");
        listModel.addElement("Snack");

        categoryList = new JList<>(listModel);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton selectButton = new JButton("Pilih");
        selectButton.addActionListener(e -> {
            selectedCategory = categoryList.getSelectedValue();
            dispose();
        });

        setLayout(new BorderLayout());
        add(new JScrollPane(categoryList), BorderLayout.CENTER);
        add(selectButton, BorderLayout.SOUTH);
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }
}