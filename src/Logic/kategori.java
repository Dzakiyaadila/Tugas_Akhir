package Logic;

import java.util.List;

public class kategori {
    private int id;
    private String nama;
//    private List<supplier> supplierList; //buat relasi ke supplier

    public kategori (int id, String nama){
        this.id = id;
        this.nama = nama;
        // Inisialisasi supplierList jika memang akan digunakan
        // this.supplierList = new ArrayList<>();
    }

    public int getId(){
        return id;
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String namaBaru){
        this.nama = namaBaru;
    }

//    public List<supplier> getSupplierList() {
//        return supplierList;
//    }

//    public void setSupplierList(List<supplier> supplierList) {
//        this.supplierList = supplierList;
//    }

    // Untuk menyimpan ke CSV (hanya ID dan Nama)
    public String toCsvString() {
        return id + "," + nama;
    }

    // Untuk membaca dari CSV
    public static kategori fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 2) {
            try {
                int id = Integer.parseInt(parts[0].trim());
                String nama = parts[1].trim();
                return new kategori(id, nama);
            } catch (NumberFormatException e) {
                System.err.println("Lewati baris tidak valid di CSV kategori: " + csvLine);
            }
        } else {
            System.err.println("Lewati baris dengan format kolom tidak sesuai di CSV kategori: " + csvLine);
        }
        return null;
    }

    @Override
    public String toString(){
        return id + " -- " + nama;
    }
}