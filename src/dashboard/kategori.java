package dashboard;

public class kategori {
    private int id;
    private String nama;

    public kategori (int id, String nama){
        this.id = id;
        this.nama = nama;
    }
    public int getId(){
        return id;
    }
    public String getNama(){
        return getNama();
    }
    public void setNama(String namaBaru){
        this.nama = namaBaru;
    }
    @Override
    public String toString(){
        return id + " -- " + nama;
    }
}
