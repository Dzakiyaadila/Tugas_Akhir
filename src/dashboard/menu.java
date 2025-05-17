package dashboard;

public class menu {
    private int id;
    private String nama;
    private double harga;
    private kategori kategori;

    public menu (int id, String nama, double harga, kategori kategori){
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    public int getId (){
        return id;
    }
    public String getNama(){
        return nama;
    }
    public double getHarga(){
        return harga;
    }
    public kategori kategori(){
        return kategori;
    }
    public void setNama(String namaBaru){
        this.nama = namaBaru;
    }
    public void setHarga (double hargaBaru){
        this.harga = hargaBaru;
    }
    public void setKategori (kategori kategoriBaru){
        this.kategori = kategoriBaru;
    }
    @Override
    public String toString(){
        return id + " || " + nama + " || Rp." + harga + " || Kategori: " + kategori.getNama();
    }
}
