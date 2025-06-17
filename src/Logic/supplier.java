package Logic;

import CRUD.repoKategori;

public class supplier {
    private int id;
    private String nama;
    private String telepon;
    private String alamat;
    private kategori kategori; // properti objek kategori yang disuplai

    public supplier(int id, String nama, String telepon, String alamat, kategori kategori) {
        this.id = id;
        this.nama = nama;
        this.telepon = telepon;
        this.alamat = alamat;
        this.kategori = kategori;
    }

    // getters
    public int getId() { return id; }
    public String getNama() { return nama; }
    public String getTelepon() { return telepon; }
    public String getAlamat() { return alamat; }
    public kategori getKategori() { return kategori; }

    // setters (untuk mengedit)
    public void setNama(String nama) { this.nama = nama; }
    public void setTelepon(String telepon) { this.telepon = telepon; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public void setKategori(kategori kategori) { this.kategori = kategori; }

    // method untuk mengubah objek Supplier menjadi baris CSV
    public String toCsvString() {
        // Format: id,nama,telepon,alamat,idKategori
        // Kita simpan ID kategorinya, bukan objek kategori, di CSV
        return String.format("%d,%s,%s,%s,%d", id, nama, telepon, alamat, kategori.getId()); // <-- UBAH INI
    }

    // method statis untuk membuat objek supplier dari baris CSV
    // method ini akan perlu akses ke repoKategori untuk menemukan objek kategori berdasarkan ID
    public static supplier fromCsvString(String csvLine, repoKategori kategoriRepo) {
        String[] parts = csvLine.split(",");
        if (parts.length == 5) { // id,nama,telepon,alamat,idKategori
            try {
                int id = Integer.parseInt(parts[0]);
                String nama = parts[1];
                String telepon = parts[2];
                String alamat = parts[3];
                int kategoriId = Integer.parseInt(parts[4]); // <-- TAMBAHKAN INI

                // Cari objek kategori dari repoKategori
                kategori associatedKategori = kategoriRepo.getId(kategoriId);
                if (associatedKategori != null) {
                    return new supplier(id, nama, telepon, alamat, associatedKategori); // <-- UBAH INI
                } else {
                    System.err.println("Kategori dengan ID " + kategoriId + " tidak ditemukan untuk supplier: " + nama);
                }
            } catch (NumberFormatException e) {
                System.err.println("Format angka salah di baris CSV supplier: " + csvLine);
            }
        } else {
            System.err.println("Jumlah kolom supplier tidak sesuai: " + csvLine + ". Expected 5, found " + parts.length);
        }
        return null;
    }

    @Override
    public String toString() {
        return id + " - " + nama + " (" + (kategori != null ? kategori.getNama() : "N/A") + ")"; // Tambahkan kategori di toString
    }
}