package repository;
import dashboard.kategori;
import java.util.ArrayList;
import java.util.List;


public class repoKategori {
    private List<kategori> categories;
    private int id = 1;

    public repoKategori(){
        categories = new ArrayList<>();
        addkategori("Makanan");
        addkategori("Minuman");
        addkategori("Alat Tulis");
    }
    public List<kategori> getCategories() {
        return categories;
    }
    //untuk nambahin kategori
    private void addkategori(String nama) {
        categories.add(new kategori(id++, nama));
    }
    //untuk edit kategori (nama kategori)
    public boolean updateKategori(int id, String namaBaru){
        for(kategori k : categories){
            if(k.getId()== id){
                k.setNama(namaBaru);
                return true;
            }
        }
        return false;
    }
    //untuk hapus kategori by id
    public boolean deletKategori(int id){
        return categories.removeIf(kategori -> kategori.getId()==id);
    }
    // read kategori
    public kategori getId(int id){
        for (kategori kategori : categories){
            if(kategori.getId()==id);
            return kategori;
        }
        return null;
    }

}
