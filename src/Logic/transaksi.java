package Logic;

import java.time.LocalDateTime;
import java.util.List;

public class transaksi {
    private int idTransaksi; // tambahkan ini
    private int userId;
    private double total;
    private String metodePembayaran;
    private LocalDateTime waktu;
    private List<transaksiDetail> detailList;

    // memperbarui konstruktor atau tambahkan konstruktor baru
    public transaksi(int userId, double total, String metodePembayaran, List<transaksiDetail> detailList) {
        this.userId = userId;
        this.total = total;
        this.metodePembayaran = metodePembayaran;
        this.waktu = LocalDateTime.now();
        this.detailList = detailList;
    }


    public void setTransaksiId(int idTransaksi) { this.idTransaksi = idTransaksi; }
    public int getIdTransaksi() { return idTransaksi; }
    public int getUserId() { return userId; }
    public double getTotal() { return total; }
    public String getMetodePembayaran() { return metodePembayaran; }
    public LocalDateTime getWaktu() { return waktu; }
    public List<transaksiDetail> getDetailList() { return detailList; }
}
