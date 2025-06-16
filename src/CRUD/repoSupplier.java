package CRUD;

import Logic.CSVHelper;
import Logic.supplier;
import Logic.kategori;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class repoSupplier {
    private List<supplier> suppliers;
    private int nextId;
    private repoKategori kategoriRepo; // akses ke repoKategori
    private static final String SUPPLIER_FILE = "data/supplier.csv";

    public repoSupplier(repoKategori kategoriRepo) {
        this.kategoriRepo = kategoriRepo;
        this.suppliers = new ArrayList<>();
        new File("data").mkdirs();
        loadSuppliers();
        this.nextId = getNextAvailableSupplierId();
    }

    private int getNextAvailableSupplierId() {
        int maxId = 0;
        if (!suppliers.isEmpty()) {
            for (supplier s : suppliers) {
                if (s.getId() > maxId) {
                    maxId = s.getId();
                }
            }
        }
        return maxId + 1;
    }

    public void loadSuppliers() {
        suppliers.clear();
        List<String[]> data = CSVHelper.readCSV(SUPPLIER_FILE);
        if (!data.isEmpty() && data.get(0).length > 0 && data.get(0)[0].equals("id")) { // untuk mengcek header
            data.remove(0);
        }
        for (String[] row : data) {
            if (row.length > 0) {
                String csvLine = String.join(",", row);
                // memanggil fromCsvString dari kelas supplier, yang kini membutuhkan repoKategori
                supplier s = supplier.fromCsvString(csvLine, kategoriRepo);
                if (s != null) {
                    suppliers.add(s);
                }
            }
        }
    }

    public void saveSuppliers() {
        List<String[]> dataToSave = new ArrayList<>();
        dataToSave.add(new String[]{"id", "nama", "telepon", "alamat", "idKategori"});
        for (supplier s : suppliers) {
            dataToSave.add(s.toCsvString().split(","));
        }
        CSVHelper.writeCSV(SUPPLIER_FILE, dataToSave);
    }

    // === Metode CRUD ===

    public List<supplier> getSuppliers() {
        return suppliers;
    }

    public void addSupplier(String nama, String telepon, String alamat, kategori kategoriSupplier) {
        suppliers.add(new supplier(nextId++, nama, telepon, alamat, kategoriSupplier));
        saveSuppliers();
    }

    public supplier getSupplierById(int id) {
        for (supplier s : suppliers) {
            if (s.getId() == id) {
                return s;
            }
        }
        return null;
    }

    public boolean updateSupplier(int id, String namaBaru, String teleponBaru, String alamatBaru, kategori kategoriBaru) {
        supplier foundSupplier = getSupplierById(id);
        if (foundSupplier != null) {
            foundSupplier.setNama(namaBaru);
            foundSupplier.setTelepon(teleponBaru);
            foundSupplier.setAlamat(alamatBaru);
            foundSupplier.setKategori(kategoriBaru);
            saveSuppliers();
            return true;
        }
        return false;
    }

    public boolean deleteSupplier(int id) {
        boolean removed = suppliers.removeIf(s -> s.getId() == id);
        if (removed) {
            saveSuppliers();
        }
        return removed;
    }
}
