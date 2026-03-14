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
import View.KategoriDataChangeListener; // Import interface listener

// PublicFrame sekarang mengimplementasikan kedua listener
public class PublicFrame extends JPanel implements MenuDataChangeListener, KategoriDataChangeListener {
    private JTabbedPane tabbedPane;
    private repoMenu menuRepository;
    private repoKategori kategoriRepository; // Tambahkan referensi ke repoKategori

    // Modifikasi konstruktor untuk menerima repoKategori
    public PublicFrame(repoMenu menuRepository, repoKategori kategoriRepository) {
        this.menuRepository = menuRepository;
        this.menuRepository.addMenuDataChangeListener(this); // Mendaftar sebagai listener perubahan Menu

        this.kategoriRepository = kategoriRepository; // Inisialisasi repoKategori
        this.kategoriRepository.addKategoriDataChangeListener(this); // Mendaftar sebagai listener perubahan Kategori

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

        // Panggil updateMenuDisplay saat inisialisasi awal
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

    // Implementasi metode dari KategoriDataChangeListener
    @Override
    public void onKategoriDataChanged(List<kategori> updatedKategoriList) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("PublicFrame: Menerima notifikasi perubahan kategori. Memperbarui UI menu.");
            // Ketika kategori berubah, kita perlu membangun ulang tampilan menu karena pengelompokan berdasarkan kategori.
            // Oleh karena itu, panggil updateMenuDisplay dengan daftar menu terbaru dari repoMenu.
            updateMenuDisplay(menuRepository.getListMenu());
        });
    }

    private void updateMenuDisplay(List<menu> menuList) {
        tabbedPane.removeAll(); // Hapus semua tab yang ada

        // Panel untuk "All Menu"
        JPanel allMenuPanel = new JPanel(new GridLayout(0, 2, 15, 15));
        allMenuPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        allMenuPanel.setBackground(Color.WHITE);

        // Pastikan kategori yang ada di menu masih valid setelah perubahan kategori
        // atau tangani kategori yang mungkin sudah dihapus
        Map<String, List<menu>> categorizedMenus = menuList.stream()
                .filter(m -> m.getKategori() != null) // Filter menu dengan kategori null (jika ada yang tidak valid)
                .collect(Collectors.groupingBy(m -> m.getKategori().getNama()));

        // Tambahkan tab untuk "All Menu" di posisi 0
        // Tambahkan semua item menu ke allMenuPanel
        for (menu item : menuList) {
            allMenuPanel.add(createMenuItemCard(item.getNama(), String.format("%,.0f", item.getHarga()), item.getStok()));
        }
        tabbedPane.addTab("All Menu", new JScrollPane(allMenuPanel));


        // Tambahkan tab untuk setiap kategori
        categorizedMenus.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String category = entry.getKey();
                    List<menu> items = entry.getValue();

                    JPanel categorySpecificPanel = new JPanel(new GridLayout(0, 2, 15, 15));
                    categorySpecificPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
                    categorySpecificPanel.setBackground(Color.WHITE);

                    for (menu item : items) {
                        categorySpecificPanel.add(createMenuItemCard(item.getNama(), String.format("%,.0f", item.getHarga()), item.getStok()));
                    }
                    tabbedPane.addTab(category, new JScrollPane(categorySpecificPanel));
                });

        // Pastikan "All Menu" tetap di indeks 0 jika ada kategori lain yang ditambahkan
        // Jika Anda ingin "All Menu" selalu pertama, pastikan penambahannya di awal atau setelah semua tab kategori ditambahkan
        // dan kemudian pindah ke indeks 0. Cara di atas (tambah All Menu duluan lalu loop kategori) seharusnya sudah benar.
    }

    private JPanel createMenuItemCard(String name, String price, int stok) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLabel.setForeground(new Color(50, 50, 50));
        textPanel.add(nameLabel, BorderLayout.NORTH);

        JLabel priceLabel = new JLabel("Rp " + price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(new Color(0, 120, 215));
        priceLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        textPanel.add(priceLabel, BorderLayout.CENTER);

        JLabel stockLabel = new JLabel("Stok: " + stok + " unit");
        stockLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        stockLabel.setForeground(new Color(0, 120, 215));
        stockLabel.setBorder(new EmptyBorder(5, 0, 0, 0));
        textPanel.add(stockLabel, BorderLayout.SOUTH);

        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(900, 650);
    }

    // Metode main untuk testing (opsional, bisa dihapus jika sudah terintegrasi ke mainFrame)
//    public static void main(String[] args) {
//        CRUD.repoKategori kategoriRepo = new CRUD.repoKategori();
//        repoMenu menuRepo = new repoMenu(kategoriRepo);
//
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Canteen Menu Display");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            // Teruskan kedua repo ke PublicFrame
//            frame.add(new PublicFrame(menuRepo, kategoriRepo));
//            frame.pack();
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//
//            // SIMULASI PERUBAHAN KATEGORI (sebagai contoh)
//            Timer addKategoriTimer = new Timer(2000, e -> {
//                kategoriRepo.addkategori("Dessert");
//                System.out.println("SIMULASI: Menambahkan kategori 'Dessert'");
//            });
//            addKategoriTimer.setRepeats(false);
//            addKategoriTimer.start();
//
//            Timer updateKategoriTimer = new Timer(4000, e -> {
//                kategori existingMinuman = kategoriRepo.getKategoriByName("Minuman");
//                if (existingMinuman != null) {
//                    kategoriRepo.updateKategori(existingMinuman.getId(), "Beverages");
//                    System.out.println("SIMULASI: Mengubah nama kategori 'Minuman' menjadi 'Beverages'");
//                }
//            });
//            updateKategoriTimer.setRepeats(false);
//            updateKategoriTimer.start();
//
//
//            Timer addTimer = new Timer(3000, e -> {
//                int newId = menuRepo.getListMenu().stream().mapToInt(menu::getId).max().orElse(0) + 1;
//                Logic.kategori newCategory = kategoriRepo.getKategoriByName("MAKANAN");
//                if (newCategory == null) {
//                    kategoriRepo.addkategori("MAKANAN");
//                    newCategory = kategoriRepo.getKategoriByName("MAKANAN");
//                    System.out.println("SIMULASI: Menambahkan kategori MAKANAN (jika belum ada).");
//                }
//
//                if (newCategory != null) {
//                    menuRepo.addMenu("Nasi Goreng Spesial", 38000.00, newCategory, 12, "Vendor A");
//                    System.out.println("SIMULASI: Menambah Nasi Goreng Spesial");
//                } else {
//                    System.err.println("Gagal mendapatkan/membuat kategori 'MAKANAN'. Tidak dapat menambahkan menu.");
//                }
//            });
//            // addTimer.setRepeats(false); // Komentar ini karena Timer di atas juga akan memicu menu update
//            // addTimer.start(); // Komentar ini untuk menghindari terlalu banyak timer, fokus pada kategori dulu
//
//            Timer updatePriceTimer = new Timer(6000, e -> {
//                menu beefSteak = menuRepo.getListMenu().stream()
//                        .filter(m -> m.getNama().equals("Beef Steak"))
//                        .findFirst().orElse(null);
//                if (beefSteak != null) {
//                    menuRepo.updateMenu(beefSteak.getId(), beefSteak.getNama(), 90000.00, beefSteak.getKategori(), beefSteak.getStok(), beefSteak.getSupplier());
//                    System.out.println("SIMULASI: Memperbarui harga Beef Steak menjadi 90,000");
//                }
//            });
//            updatePriceTimer.setRepeats(false);
//            updatePriceTimer.start();
//
//            Timer updateStockTimer = new Timer(9000, e -> {
//                menu icedLatte = menuRepo.getListMenu().stream()
//                        .filter(m -> m.getNama().equals("Iced Latte"))
//                        .findFirst().orElse(null);
//                if (icedLatte != null) {
//                    menuRepo.updateStok(icedLatte.getId(), 10);
//                    System.out.println("SIMULASI: Memperbarui stok Iced Latte menjadi 10");
//                }
//            });
//            updateStockTimer.setRepeats(false);
//            updateStockTimer.start();
//        });
//    }
}