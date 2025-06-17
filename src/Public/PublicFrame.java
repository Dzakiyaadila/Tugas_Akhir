package Public;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import CRUD.repoMenu;
import Logic.menu;
import Logic.kategori;
import View.MenuDataChangeListener;
import CRUD.repoKategori;


public class PublicFrame extends JPanel implements MenuDataChangeListener {
    private JTabbedPane tabbedPane;
    private repoMenu menuRepository;

    public PublicFrame(repoMenu menuRepository) {
        this.menuRepository = menuRepository;
        this.menuRepository.addMenuDataChangeListener(this);

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. Header Panel
        add(createHeaderPanel(), BorderLayout.NORTH);

        // 2. Main Content - Menu Display
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(new Color(0, 120, 215));
        add(tabbedPane, BorderLayout.CENTER);

        // 3. Footer
        add(createFooterPanel(), BorderLayout.SOUTH);

        updateMenuDisplay(this.menuRepository.getListMenu());
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Canteen", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Montserrat", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, BorderLayout.CENTER);

        JLabel subtitleLabel = new JLabel("Our Menu", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 230, 255));
        panel.add(subtitleLabel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel footerLabel = new JLabel("© 2025 Canteen - All Rights Reserved");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 150, 150));
        panel.add(footerLabel);

        return panel;
    }

    @Override
    public void onMenuDataChanged(List<menu> updatedMenuList) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("PublicFrame: Menerima notifikasi perubahan menu. Memperbarui UI...");
            updateMenuDisplay(updatedMenuList);
        });
    }

    private void updateMenuDisplay(List<menu> menuList) {
        tabbedPane.removeAll();

        // Panel untuk "All Menu"
        JPanel allMenuPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        allMenuPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        allMenuPanel.setBackground(Color.WHITE);

        Map<String, List<menu>> categorizedMenus = menuList.stream()
                .collect(Collectors.groupingBy(m -> m.getKategori().getNama()));

        categorizedMenus.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String category = entry.getKey();
                    List<menu> items = entry.getValue();

                    JPanel categoryPanel = new JPanel(new GridLayout(0, 2, 15, 15));
                    categoryPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                    categoryPanel.setBackground(Color.WHITE);

                    for (menu item : items) {
                        categoryPanel.add(createMenuItemCard(item.getNama(), String.format("%,.0f", item.getHarga())));
                        allMenuPanel.add(createMenuItemCard(item.getNama(), String.format("%,.0f", item.getHarga())));
                    }
                    tabbedPane.addTab(category, new JScrollPane(categoryPanel));
                });

        tabbedPane.insertTab("All Menu", null, new JScrollPane(allMenuPanel), null, 0);
    }

    // --- MODIFIKASI: createMenuItemCard() tanpa imagePlaceholder ---
    private JPanel createMenuItemCard(String name, String price) {
        JPanel card = new JPanel(new BorderLayout()); // Menggunakan BorderLayout
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15) // Padding internal
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Panel untuk nama dan harga
        JPanel textPanel = new JPanel(new BorderLayout()); // Menggunakan BorderLayout untuk teks
        textPanel.setOpaque(false); // Agar background transparan

        // Menu Name
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        textPanel.add(nameLabel, BorderLayout.NORTH); // Nama di bagian atas textPanel

        // Price
        JLabel priceLabel = new JLabel("Rp " + price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 120, 215));
        priceLabel.setBorder(new EmptyBorder(5, 0, 0, 0)); // Sedikit padding dari nama
        textPanel.add(priceLabel, BorderLayout.CENTER); // Harga di bagian tengah textPanel

        card.add(textPanel, BorderLayout.CENTER); // Tambahkan textPanel ke tengah card

        return card;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 650);
    }

    // Metode main untuk testing (opsional, bisa dihapus jika sudah terintegrasi ke mainFrame)
    public static void main(String[] args) {
        CRUD.repoKategori kategoriRepo = new CRUD.repoKategori();
        repoMenu menuRepo = new repoMenu(kategoriRepo);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Canteen Menu Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new PublicFrame(menuRepo));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            Timer addTimer = new Timer(3000, e -> {
                int newId = menuRepo.getListMenu().stream().mapToInt(menu::getId).max().orElse(0) + 1;
                Logic.kategori newCategory = kategoriRepo.getKategoriByName("MAKANAN");
                if (newCategory == null) {
                    // Perbaikan di sini: Jangan langsung membuat kategori dengan ID hardcode
                    kategoriRepo.addkategori("MAKANAN"); // Biarkan repoKategori yang mengelola ID
                    newCategory = kategoriRepo.getKategoriByName("MAKANAN");
                    System.out.println("SIMULASI: Menambahkan kategori MAKANAN (jika belum ada).");
                }

                if (newCategory != null) {
                    menuRepo.addMenu("Nasi Goreng Spesial", 38000.00, newCategory, 12, "Vendor A");
                    System.out.println("SIMULASI: Menambah Nasi Goreng Spesial");
                } else {
                    System.err.println("Gagal mendapatkan/membuat kategori 'MAKANAN'. Tidak dapat menambahkan menu.");
                }
            });
            addTimer.setRepeats(false);
            addTimer.start();

            Timer updatePriceTimer = new Timer(6000, e -> {
                menu beefSteak = menuRepo.getListMenu().stream()
                        .filter(m -> m.getNama().equals("Beef Steak"))
                        .findFirst().orElse(null);
                if (beefSteak != null) {
                    menuRepo.updateMenu(beefSteak.getId(), beefSteak.getNama(), 90000.00, beefSteak.getKategori(), beefSteak.getStok(), beefSteak.getSupplier());
                    System.out.println("SIMULASI: Memperbarui harga Beef Steak menjadi 90,000");
                }
            });
            updatePriceTimer.setRepeats(false);
            updatePriceTimer.start();

            Timer updateStockTimer = new Timer(9000, e -> {
                menu icedLatte = menuRepo.getListMenu().stream()
                        .filter(m -> m.getNama().equals("Iced Latte"))
                        .findFirst().orElse(null);
                if (icedLatte != null) {
                    menuRepo.updateStok(icedLatte.getId(), 10);
                    System.out.println("SIMULASI: Memperbarui stok Iced Latte menjadi 10");
                }
            });
            updateStockTimer.setRepeats(false);
            updateStockTimer.start();
        });
    }
}