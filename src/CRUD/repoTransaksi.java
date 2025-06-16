package CRUD;

import Logic.CSVHelper;
import Logic.transaksi;
import Logic.transaksiDetail;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class repoTransaksi {
    private static final String TRANSAKSI_FILE = "data/transaksi.csv";
    private static final String DETAIL_TRANSAKSI_FILE = "data/transaksi_detail.csv";
    private int nextTransaksiId; // Untuk generate ID transaksi

    public repoTransaksi() {
        // Pastikan direktori 'data' ada
        new java.io.File("data").mkdirs();
        loadTransaksi(); // Muat transaksi yang sudah ada saat inisialisasi
        // Cari ID transaksi terakhir untuk melanjutkan ID
        this.nextTransaksiId = getNextAvailableTransaksiId();
    }

    private int getNextAvailableTransaksiId() {
        List<String[]> data = CSVHelper.readCSV(TRANSAKSI_FILE);
        int maxId = 0;
        // Asumsi baris pertama adalah header, atau ID dimulai dari 1
        if (!data.isEmpty() && data.get(0).length > 0 && !data.get(0)[0].equals("idTransaksi")) { // Cek bukan header
            for (String[] row : data) {
                try {
                    int id = Integer.parseInt(row[0]);
                    if (id > maxId) {
                        maxId = id;
                    }
                } catch (NumberFormatException e) {
                    // Abaikan baris yang tidak valid atau header
                }
            }
        }
        return maxId + 1;
    }


    // Metode untuk menyimpan transaksi baru
    public void saveTransaksi(transaksi trx) {
        // Siapkan data untuk transaksi.csv
        List<String[]> allTransaksiData = CSVHelper.readCSV(TRANSAKSI_FILE);
        // Tambahkan header jika file baru
        if (allTransaksiData.isEmpty()) {
            allTransaksiData.add(new String[]{"idTransaksi", "userId", "waktu", "metodePembayaran", "total"});
        }
        int currentTransaksiId = nextTransaksiId++; // Ambil ID dan increment
        trx.setTransaksiId(currentTransaksiId); // Set ID ke objek transaksi

        String transaksiLine = String.format("%d,%d,%s,%s,%.2f",
                currentTransaksiId,
                trx.getUserId(),
                trx.getWaktu().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                trx.getMetodePembayaran(),
                trx.getTotal());
        allTransaksiData.add(transaksiLine.split(","));
        CSVHelper.writeCSV(TRANSAKSI_FILE, allTransaksiData);

        // Siapkan data untuk transaksi_detail.csv
        List<String[]> allDetailData = CSVHelper.readCSV(DETAIL_TRANSAKSI_FILE);
        if (allDetailData.isEmpty()) {
            allDetailData.add(new String[]{"idTransaksi", "menuId", "jumlah", "subtotal"});
        }
        for (transaksiDetail detail : trx.getDetailList()) {
            String detailLine = String.format("%d,%s,%d,%.2f",
                    currentTransaksiId,
                    detail.getMenuId(),
                    detail.getJumlah(),
                    detail.getSubtotal());
            allDetailData.add(detailLine.split(","));
        }
        CSVHelper.writeCSV(DETAIL_TRANSAKSI_FILE, allDetailData);

        System.out.println("Transaksi ID " + currentTransaksiId + " berhasil disimpan.");
    }

    // Metode untuk memuat semua transaksi
    public List<transaksi> loadTransaksi() {
        List<transaksi> daftarTransaksi = new ArrayList<>();
        List<String[]> transaksiData = CSVHelper.readCSV(TRANSAKSI_FILE);
        List<String[]> detailData = CSVHelper.readCSV(DETAIL_TRANSAKSI_FILE);

        // Melewatkan header jika ada
        if (!transaksiData.isEmpty() && transaksiData.get(0).length > 0 && transaksiData.get(0)[0].equals("idTransaksi")) {
            transaksiData.remove(0);
        }
        if (!detailData.isEmpty() && detailData.get(0).length > 0 && detailData.get(0)[0].equals("idTransaksi")) {
            detailData.remove(0);
        }


        for (String[] row : transaksiData) {
            try {
                int idTransaksi = Integer.parseInt(row[0]);
                int userId = Integer.parseInt(row[1]);
                LocalDateTime waktu = LocalDateTime.parse(row[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                String metodePembayaran = row[3];
                double total = Double.parseDouble(row[4]);

                List<transaksiDetail> details = new ArrayList<>();
                for (String[] detailRow : detailData) {
                    if (Integer.parseInt(detailRow[0]) == idTransaksi) {
                        details.add(new transaksiDetail(
                                detailRow[1], // menuId
                                Integer.parseInt(detailRow[2]), // jumlah
                                Double.parseDouble(detailRow[3]) // subtotal
                        ));
                    }
                }
                // Perlu menambahkan konstruktor di transaksi yang menerima idTransaksi
                transaksi trx = new transaksi(userId, total, metodePembayaran, details);
                // Mungkin perlu setter untuk ID transaksi jika konstruktor tidak menerima
                trx.setTransaksiId(idTransaksi);
                daftarTransaksi.add(trx);

            } catch (NumberFormatException | IndexOutOfBoundsException | java.time.format.DateTimeParseException e) {
                System.err.println("Skipping malformed transaction row: " + String.join(",", row) + " Error: " + e.getMessage());
            }
        }
        return daftarTransaksi;
    }
}