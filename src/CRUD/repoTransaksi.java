package CRUD;


import Logic.CSVUtil;
import Logic.transaksi;

import java.util.ArrayList;
import java.util.List;

public class repoTransaksi {
    private int nextTransaksiId;
    private List<transaksi> daftarTransaksi = new ArrayList<>();

    public repoTransaksi() {
        this.daftarTransaksi = loadTransaksi(); // load saat inisialisasi
        this.nextTransaksiId = getNextAvailableTransaksiId();
    }

    private int getNextAvailableTransaksiId() {
        int maxId = 0;
        for (transaksi trx : daftarTransaksi) {
            if (trx.getIdTransaksi() > maxId) {
                maxId = trx.getIdTransaksi();
            }
        }
        return maxId + 1;
    }

    public void saveTransaksi(transaksi trx) {
        trx.setTransaksiId(nextTransaksiId++);
        daftarTransaksi.add(trx);

        // Simpan ke file pakai CSVUtil
        CSVUtil.simpanTransaksi(trx);

        System.out.println("Transaksi berhasil disimpan dengan ID: " + trx.getIdTransaksi());
    }

    public List<transaksi> loadTransaksi() {
        // Sementara kosongkan, karena tidak ada implementasi read dari format struk.
        // Tapi bisa ditambahkan kalau nanti mau parsing dari transaksi.csv
        return new ArrayList<>();
    }

    public List<transaksi> getSemuaTransaksi() {
        return daftarTransaksi;
    }
}
