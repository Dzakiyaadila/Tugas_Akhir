package Logic;

import java.util.List;

public class kategori {
    private int id;
    private String nama;
    private List<supplier> supplierList; //buat relasi ke supplier

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

    public List<supplier> getSupplierList() {
        return supplierList;
    }

    public void setSupplierList(List<supplier> supplierList) {
        this.supplierList = supplierList;
    }

    @Override
    public String toString(){
        return id + " -- " + nama;
    }
}