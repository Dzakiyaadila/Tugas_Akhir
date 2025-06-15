package CRUD;
import Logic.kategori;
import java.util.ArrayList;
import java.util.List;


public class repoKategori {
    private List<kategori> categories;
    private int nextId;

    public repoKategori(){
        categories = new ArrayList<>();
        this.nextId = 1; // mulai ID dari 1
        addkategori("Makanan");
        addkategori("Minuman");
        addkategori("Alat Tulis");
    }

    public List<kategori> getCategories() {
        return categories;
    }

    public void addkategori(String nama) { // ganti private jadi public
        categories.add(new kategori(nextId++, nama));
    }

    // edit kategori (nama kategori)
    public boolean updateKategori(int id, String namaBaru){
        for(kategori k : categories){
            if(k.getId()== id){
                k.setNama(namaBaru);
                return true;
            }
        }
        return false;
    }

    // hapus kategori by id
    public boolean deletKategori(int id){
        return categories.removeIf(kategori -> kategori.getId()==id);
    }

    // read kategori by ID
    public kategori getId(int id){
        for (kategori kategori : categories){
            if(kategori.getId() == id){
                return kategori;
            }
        }
        return null; // kalau tidak ditemukan
    }
}