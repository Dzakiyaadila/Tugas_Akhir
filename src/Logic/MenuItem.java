// File: Logic/MenuItem.java
package Logic;

public class MenuItem {
    private String nama, kategori, supplier;
    private int harga, stok;

    public MenuItem(String nama, String kategori, int harga, int stok, String supplier) {
        this.nama = nama;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.supplier = supplier;
    }

    public String getNama() { return nama; }
    public String getKategori() { return kategori; }
    public int getHarga() { return harga; }
    public int getStok() { return stok; }
    public String getSupplier() { return supplier; }
}
