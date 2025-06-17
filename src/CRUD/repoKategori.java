package CRUD;

import Logic.kategori;
import Logic.menu;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import View.KategoriDataChangeListener; // Import interface listener

public class repoKategori {
    private List<kategori> categories;
    private int nextId;
    private repoMenu menuRepo;
    private List<KategoriDataChangeListener> kategoriListeners = new ArrayList<>();

    // Definisikan nama file CSV untuk kategori
    private static final String CATEGORIES_CSV_FILE = "data/kategori.csv"; //

    public repoKategori(){
        categories = new ArrayList<>();
        // Coba muat kategori dari file saat inisialisasi
        loadCategoriesFromFile(); //

        // Jika tidak ada kategori yang dimuat (file tidak ada atau kosong), inisialisasi default
        if (categories.isEmpty()) { //
            this.nextId = 1;
            addkategori("Makanan"); // Memanggil addkategori, yang akan memicu save dan notifikasi
            addkategori("Minuman"); //
            addkategori("Alat Tulis"); //
            // Tidak perlu memanggil saveCategoriesToFile() lagi di sini karena addkategori() sudah melakukannya
        } else {
            // Jika ada kategori yang dimuat, tentukan nextId berdasarkan ID tertinggi yang ada
            this.nextId = categories.stream().mapToInt(k -> k.getId()).max().orElse(0) + 1; //
        }
    }

    // setter untuk menuRepo
    public void setMenuRepo(repoMenu menuRepo){
        this.menuRepo = menuRepo;
    }

    // Metode untuk menambahkan listener
    public void addKategoriDataChangeListener(KategoriDataChangeListener listener) {
        this.kategoriListeners.add(listener);
    }

    // Metode untuk menghapus listener (opsional, tapi praktik baik)
    public void removeKategoriDataChangeListener(KategoriDataChangeListener listener) {
        this.kategoriListeners.remove(listener);
    }

    public List<kategori> getCategories() {
        return categories;
    }

    public void addkategori(String nama) {
        categories.add(new kategori(nextId++, nama));
        saveCategoriesToFile(); // Simpan perubahan ke file
        notifyKategoriDataChanged(); // Beri tahu listener setelah data berubah
    }

    // edit kategori (nama kategori)
    public boolean updateKategori(int id, String namaBaru){
        for(kategori k : categories){
            if(k.getId()== id){
                k.setNama(namaBaru);
                saveCategoriesToFile(); // Simpan perubahan ke file
                notifyKategoriDataChanged(); // Beri tahu listener setelah data berubah
                return true;
            }
        }
        return false;
    }

    // hapus kategori by id
    public boolean deleteKategori(int id){
        kategori kategoriToDelete = getId(id);
        if (kategoriToDelete == null) {
            System.out.println("repoKategori: Gagal menghapus. Kategori dengan ID " + id + " tidak ditemukan.");
            return false;
        }

        // --- LOGIKA CASCADE DELETE KE MENU ---
        // PASTIKAN menuRepo TIDAK NULL sebelum memanggilnya
        if (this.menuRepo != null) {
            System.out.println("repoKategori: Memulai cascade delete untuk kategori: " + kategoriToDelete.getNama());
            List<menu> allMenusBeforeDeletion = new ArrayList<>(menuRepo.getListMenu());
            List<menu> menusToDelete = new ArrayList<>();

            for (menu m : allMenusBeforeDeletion) {
                // Pastikan m.getKategori() tidak null sebelum memanggil getId()
                if (m.getKategori() != null && m.getKategori().getId() == kategoriToDelete.getId()) {
                    menusToDelete.add(m);
                }
            }

            System.out.println("repoKategori: Ditemukan " + menusToDelete.size() + " menu yang terkait dengan kategori '" + kategoriToDelete.getNama() + "'.");
            for (menu m : menusToDelete) {
                System.out.println("repoKategori: Menghapus menu: " + m.getNama() + " (ID: " + m.getId() + ")");
                menuRepo.deleteMenu(m.getId()); // Panggil metode deleteMenu di repoMenu
            }
        } else {
            System.err.println("repoKategori: menuRepo belum diinisialisasi untuk cascade delete kategori!");
        }
        // --- AKHIR LOGIKA CASCADE DELETE ---


        boolean removed = categories.removeIf(kategori -> kategori.getId()==id);
        if (removed) {
            saveCategoriesToFile();
            notifyKategoriDataChanged(); // Beri tahu listener
            System.out.println("repoKategori: Kategori '" + kategoriToDelete.getNama() + "' berhasil dihapus.");
            return true;
        }
        System.out.println("repoKategori: Gagal menghapus kategori: " + kategoriToDelete.getNama());
        return false;
    }

    // read kategori by ID
    public kategori getId(int id){
        for (kategori kategori : categories){
            if(kategori.getId() == id){
                return kategori;
            }
        }
        return null;
    }

    public kategori getKategoriByName(String name) {
        Optional<kategori> found = categories.stream()
                .filter(k -> k.getNama().equalsIgnoreCase(name))
                .findFirst();
        return found.orElse(null);
    }

    // Metode helper untuk memberi tahu semua listener tentang perubahan data kategori
    private void notifyKategoriDataChanged() {
        List<kategori> updatedList = new ArrayList<>(categories);
        for (KategoriDataChangeListener listener : kategoriListeners) {
            listener.onKategoriDataChanged(updatedList);
        }
    }

    // Metode untuk memuat kategori dari file CSV
    private void loadCategoriesFromFile() {
        File file = new File(CATEGORIES_CSV_FILE); //
        if (!file.exists() || file.length() == 0) { //
            System.out.println("File CSV kategori tidak ditemukan atau kosong. Inisialisasi default.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CATEGORIES_CSV_FILE))) { //
            String line;
            while ((line = reader.readLine()) != null) { //
                String[] parts = line.split(","); // Asumsi ID,NamaKategori
                if (parts.length == 2) { //
                    try {
                        int id = Integer.parseInt(parts[0].trim()); //
                        String nama = parts[1].trim(); //
                        categories.add(new kategori(id, nama)); //
                        if (id >= nextId) { // Perbarui nextId agar unik
                            nextId = id + 1;
                        }
                    } catch (NumberFormatException e) { //
                        System.err.println("Lewati baris tidak valid di CSV kategori: " + line); //
                    }
                }
            }
            System.out.println("Kategori berhasil dimuat dari " + CATEGORIES_CSV_FILE + ". Jumlah: " + categories.size());
        } catch (IOException e) { //
            System.err.println("Error saat memuat kategori dari file CSV: " + e.getMessage()); //
        }
    }

    // Metode untuk menyimpan kategori ke file CSV
    private void saveCategoriesToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CATEGORIES_CSV_FILE))) { //
            for (kategori k : categories) { //
                writer.write(k.getId() + "," + k.getNama()); //
                writer.newLine(); //
            }
            System.out.println("Kategori berhasil disimpan ke " + CATEGORIES_CSV_FILE);
        } catch (IOException e) { //
            System.err.println("Error saat menyimpan kategori ke file CSV: " + e.getMessage()); //
        }
    }
}