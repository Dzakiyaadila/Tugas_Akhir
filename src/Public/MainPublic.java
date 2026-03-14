package Public;

import CRUD.repoKategori; // Import repoKategori
import CRUD.repoMenu;     // Import repoMenu
import javax.swing.*;

public class MainPublic {
    public static void main(String[] args) {
        // Menjalankan di EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            // Buat instance repoKategori terlebih dahulu
            repoKategori kategoriRepo = new repoKategori(); //

            // Kemudian, buat instance repoMenu dan teruskan repoKategori ke dalamnya
            repoMenu menuRepo = new repoMenu(kategoriRepo); //

            // Buat frame utama
            JFrame frame = new JFrame("Canteen"); //
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //

            // Tambahkan panel GUI ke frame
            // MODIFIKASI PENTING DI SINI: Teruskan instance repoKategori juga ke PublicFrame
            frame.setContentPane(new PublicFrame(menuRepo, kategoriRepo)); //

            // Atur ukuran dan tampilkan
            frame.pack(); //
            frame.setLocationRelativeTo(null); // Pusatkan di layar
            frame.setVisible(true); //
        });
    }
}