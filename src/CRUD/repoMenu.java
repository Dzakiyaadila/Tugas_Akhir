package CRUD;
import Logic.CSVHelper;
import Logic.kategori;
import Logic.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class repoMenu {
    private List<menu> listMenu;
    private int nextId;
    private repoKategori kategoriRepo;
    private static final String MENU_FILE = "data/menu_data.csv";
    private Logic.kategori kategori;
    private int stok;
    private String supplier;

    public repoMenu(repoKategori kategoriRepo){
        this.kategoriRepo = kategoriRepo;
        this.listMenu = new ArrayList<>();
        new File("data").mkdirs();
        loadMenu();
        this.nextId = getNextAvailableMenuId();
    }

    private int getNextAvailableMenuId() {
        int maxId = 0;
        if (!listMenu.isEmpty()) {
            for (menu m : listMenu) {
                if (m.getId() > maxId) {
                    maxId = m.getId();
                }
            }
        }
        return maxId + 1;
    }

    public void loadMenu() {
        listMenu.clear();
        List<String[]> data = CSVHelper.readCSV(MENU_FILE);
        if (!data.isEmpty() && data.get(0).length > 0 && data.get(0)[0].equals("id")) {
            data.remove(0);
        }
        for (String[] row : data) {
            if (row.length > 0) {
                String csvLine = String.join(",", row);
                menu m = menu.fromCsvString(csvLine, kategoriRepo);
                if (m != null) {
                    listMenu.add(m);
                }
            }
        }
    }

    public void saveMenu() {
        List<String[]> dataToSave = new ArrayList<>();
        dataToSave.add(new String[]{"id", "nama", "harga", "idKategori", "stok", "supplier"});
        for (menu m : listMenu) {
            dataToSave.add(m.toCsvString().split(","));
        }
        CSVHelper.writeCSV(MENU_FILE, dataToSave);
    }

    public List<menu> getListMenu() {
        return listMenu;
    }


    public void addMenu(String nama, double harga, kategori kategori, int stok, String supplier){
        listMenu.add(new menu(nextId++, nama, harga, kategori, stok, supplier));
        saveMenu();
    }

    public menu getMenuById(int id) {
        for (menu m : listMenu) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public boolean updateMenu(int id, String namaBaru, double hargaBaru, kategori kategoriBaru, int stokBaru, String supplierBaru) { // <-- HARUS ADA PARAMETER INI
        menu foundMenu = getMenuById(id);
        if (foundMenu != null) {
            foundMenu.setNama(namaBaru);
            foundMenu.setHarga(hargaBaru);
            foundMenu.setKategori(kategoriBaru);
            foundMenu.setStok(stokBaru);
            foundMenu.setSupplier(supplierBaru);
            saveMenu();
            return true;
        }
        return false;
    }

    public boolean updateStok(int idMenu, int newStok) {
        menu foundMenu = getMenuById(idMenu);
        if (foundMenu != null) {
            foundMenu.setStok(newStok);
            saveMenu();
            return true;
        }
        return false;
    }

    public boolean deleteMenu(int id) {
        boolean removed = listMenu.removeIf(m -> m.getId() == id);
        if (removed) {
            saveMenu();
        }
        return removed;
    }
}