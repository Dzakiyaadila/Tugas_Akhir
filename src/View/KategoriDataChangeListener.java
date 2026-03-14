package View; // Ditempatkan di package View

import Logic.kategori;
import java.util.List;

public interface KategoriDataChangeListener {
    /**
     * Dipanggil ketika data kategori diubah (ditambah, diupdate, dihapus).
     * @param updatedKategoriList Daftar kategori terbaru.
     */
    void onKategoriDataChanged(List<kategori> updatedKategoriList);
}