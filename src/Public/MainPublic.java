package Public;

import CRUD.repoKategori; // Import repoKategori
import CRUD.repoMenu;     // Import repoMenu
import javax.swing.*;

public class MainPublic { // Baris 7: Hanya deklarasi kelas
    public static void main(String[] args) { // Baris 8: Hanya deklarasi metode main
        // Menjalankan di EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // --- BAGIAN INI SUDAH BENAR SEBELUMNYA ---
            // Buat instance repoKategori terlebih dahulu
            repoKategori kategoriRepo = new repoKategori();

            // Kemudian, buat instance repoMenu dan teruskan repoKategori ke dalamnya
            repoMenu menuRepo = new repoMenu(kategoriRepo);
            // --- AKHIR BAGIAN INI ---

            // Buat frame utama
            JFrame frame = new JFrame("Canteen");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Tambahkan panel GUI ke frame
            // --- MODIFIKASI PENTING DI SINI: Teruskan instance menuRepo ke PublicFrame ---
            frame.setContentPane(new PublicFrame(menuRepo));

            // Atur ukuran dan tampilkan
            frame.pack();
            frame.setLocationRelativeTo(null); // Pusatkan di layar
            frame.setVisible(true);
        });
    }
}