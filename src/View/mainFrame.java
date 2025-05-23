//package displayDashboard;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class mainFrame {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new loginFrame().setVisible(true);
//            // Buat frame utama sebagai container
//            JFrame mainFrame = new JFrame("Canteen System");
//            mainFrame.setSize(1000, 700);
//            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            // CardLayout untuk switch antar panel
//            CardLayout cardLayout = new CardLayout();
//            JPanel containerPanel = new JPanel(cardLayout);
//
//            // Buat semua komponen GUI
//            loginFrame loginPanel = new loginFrame();
//
//            panelDashboard dashboardPanel = new panelDashboard();
//            panelSupplier supplierPanel = new panelSupplier();
//
//            // Tambahkan semua panel ke container
//            containerPanel.add(loginPanel, "LOGIN");
//            containerPanel.add(dashboardPanel, "DASHBOARD");
//            containerPanel.add(supplierPanel, "SUPPLIER");
//
//            // Tampilkan login pertama kali
//            cardLayout.show(containerPanel, "LOGIN");
//
//            mainFrame.add(containerPanel);
//            mainFrame.setLocationRelativeTo(null);
//            mainFrame.setVisible(true);
//        });
//    }
//
//}

//
//package displayDashboard;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.ActionEvent;
//
//
//
//public class mainFrame {
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            loginFrame loginFrame = new loginFrame();
//
//            // Hanya buat SATU JFrame utama
//            JFrame mainFrame = new JFrame("Canteen System");
//            mainFrame.setSize(1000, 700);
//            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//            // CardLayout sebagai navigator
//            CardLayout cardLayout = new CardLayout();
//            JPanel containerPanel = new JPanel(cardLayout);
//
//            // Inisialisasi semua panel
//            loginFrame loginPanel = new loginFrame();
//            panelDashboard dashboardPanel = new panelDashboard();
//            panelSupplier supplierPanel = new panelSupplier();
//
//            // Tambahkan ke container dengan ID unik
//            containerPanel.add(loginPanel, "LOGIN");
//            containerPanel.add(dashboardPanel, "DASHBOARD");
//            containerPanel.add(supplierPanel, "SUPPLIER");
//
//            // Mulai dengan panel login
//            cardLayout.show(containerPanel, "LOGIN");
//
//            mainFrame.add(containerPanel);
//            mainFrame.setLocationRelativeTo(null);
//            mainFrame.setVisible(true);
//        });
//    }
//}




package View;

import javax.swing.*;

public class mainFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginFrame(); // Langsung buka dashboard
        });
    }
}
