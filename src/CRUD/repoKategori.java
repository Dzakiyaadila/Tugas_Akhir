package CRUD;

import Logic.kategori;
import java.io.*; // Import untuk operasi file
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import View.KategoriDataChangeListener; // Import interface listener

public class repoKategori {
    private List<kategori> categories;
    private int nextId;
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
    public boolean deletKategori(int id){
        boolean removed = categories.removeIf(kategori -> kategori.getId()==id);
        if (removed) {
            saveCategoriesToFile(); // Simpan perubahan ke file
            notifyKategoriDataChanged(); // Beri tahu listener setelah data berubah
        }
        return removed;
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