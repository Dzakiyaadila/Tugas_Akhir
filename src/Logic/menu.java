package Logic;

import CRUD.repoKategori;

public class menu {
    private int id;
    private String nama;
    private double harga;
    private int stok;
    private kategori kategori;
    private String supplier; // menambahkan properti supplier (sementara string dlu hihi)

    // update constructor dengan atribut baru hihi
    public menu (int id, String nama, double harga, Logic.kategori kategori, int stok, String supplier){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
        this.stok = stok;
        this.supplier = supplier;
    }


    // === GETTERS ===
    public int getId (){
        return id;
    }
    public String getNama(){
        return nama;
    }
    public double getHarga(){
        return harga;
    }
    public int getStok() {
        return stok;
    }
    public kategori getKategori(){
        return kategori;
    }
    public String getSupplier() {
        return supplier;
    }

    // === SETTERS ===
    public void setNama(String namaBaru){
        this.nama = namaBaru;
    }
    public void setHarga (double hargaBaru){
        this.harga = hargaBaru;
    }
    public void setStok(int stokBaru) {
        this.stok = stokBaru;
    }
    public void setKategori (kategori kategoriBaru){
        this.kategori = kategoriBaru;
    }
    public void setSupplier(String supplierBaru) {
        this.supplier = supplierBaru;
    }

    // === CSV METHODS ===

    // toCsvString() untuk menulis CSV
    public String toCsvString() {
        // Format: id,nama,harga,stok,idKategori,supplier
        return String.format("%d,%s,%.2f,%d,%d,%s",
                id, nama, harga, stok, kategori.getId(), supplier);
    }

    // fromCsvString() untuk membaca CSV
    public static menu fromCsvString(String csvLine, repoKategori kategoriRepo) {
        String[] parts = csvLine.split(",");
        // harusnya ada 6 bagian: id, nama, harga, stok, idKategori, supplier
        if (parts.length == 6) {
            try {
                int id = Integer.parseInt(parts[0]);
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                int stok = Integer.parseInt(parts[3]);
                int kategoriId = Integer.parseInt(parts[4]);
                String supplier = parts[5];

                kategori associatedKategori = kategoriRepo.getId(kategoriId);
                if (associatedKategori != null) {
                    return new menu(id, nama, harga, associatedKategori, stok, supplier);
                } else {
                    System.err.println("Kategori dengan ID " + kategoriId + " tidak ditemukan untuk menu: " + nama);
                }
            } catch (NumberFormatException e) {
                System.err.println("Format angka salah di baris CSV menu: " + csvLine + ". Error: " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Format baris CSV menu tidak lengkap: " + csvLine + ". Error: " + e.getMessage());
            }
        } else {
            System.err.println("Jumlah kolom tidak sesuai di baris CSV menu: " + csvLine + ". Diharapkan 6, ditemukan " + parts.length);
        }
        return null;
    }

    @Override
    public String toString(){
        return id + " || " + nama + " || Rp." + harga + " || Stok: " + stok + " || Kategori: " + kategori.getNama() + " || Supplier: " + supplier;
    }
}