package Logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {

    private static final String TRANSAKSI_FILE = "transaksi.csv";

    public static void simpanTransaksi(transaksi trx) {
        try (FileWriter writer = new FileWriter(TRANSAKSI_FILE, true)) {
            writer.write("====================================\n");
            writer.write("WAKTU       : " + trx.getWaktu() + "\n");
            writer.write("PEMBAYARAN  : " + trx.getMetodePembayaran() + "\n");
            writer.write("ITEMS:\n");

            for (transaksiDetail detail : trx.getDetailList()) {
                writer.write("- " + detail.getMenuId() + "\t" + detail.getJumlah() + " x " + detail.getSubtotal() + "\n");
            }

            writer.write("TOTAL       : " + trx.getTotal() + "\n");
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}