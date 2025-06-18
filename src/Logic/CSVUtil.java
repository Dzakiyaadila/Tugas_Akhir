package Logic;

import java.io.FileWriter;
import java.io.IOException;

public class CSVUtil {

    public static void simpanTransaksi(transaksi trx) {
        try (
                FileWriter writer = new FileWriter("data/transaksi.csv", true);
                FileWriter riwayatWriter = new FileWriter("data/riwayat_transaksi.csv", true)
        ) {
            writer.write("====================================\n");
            writer.write("WAKTU       : " + trx.getWaktu() + "\n");
            writer.write("PEMBAYARAN  : " + trx.getMetodePembayaran() + "\n");
            writer.write("ITEMS:\n");

            for (transaksiDetail detail : trx.getDetailList()) {
                writer.write("- " + detail.getMenuId() + "\t" + detail.getJumlah() + " x " + detail.getSubtotal() + "\n");

                // Tulis ke CSV riwayat
                riwayatWriter.write(trx.getUserId() + "," +
                        trx.getWaktu() + "," +
                        trx.getMetodePembayaran() + "," +
                        detail.getMenuId() + "," +
                        detail.getJumlah() + "," +
                        detail.getSubtotal() + "," +
                        trx.getTotal() + "\n");
            }

            writer.write("TOTAL       : " + trx.getTotal() + "\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}