package View;

//import app.Main;

import javax.swing.*;
import java.awt.*;

public class loginFrame extends JFrame {
    public loginFrame() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Komponen login
        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(15);
        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(15);
        JButton btnLogin = new JButton("Login");

        btnLogin.addActionListener(e -> {
            if (authenticate(txtUsername.getText(), new String(txtPassword.getPassword()))) {
//                new panelDashboard(thi);
                mainFrame mainFrame = new mainFrame(); // Buat frame utama
                mainFrame.setVisible(true); // Tampilkan
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Login gagal!");
            }
        });

        // Layout
        gbc.gridx = 0; gbc.gridy = 0;
        add(lblUsername, gbc);

        gbc.gridx = 1;
        add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(lblPassword, gbc);

        gbc.gridx = 1;
        add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);
    }

    private boolean authenticate(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }

    private void openMainApp() {
        SwingUtilities.invokeLater(() -> {
            loginFrame mainApp = new loginFrame();
            mainApp.setVisible(true);
        });
    }
}