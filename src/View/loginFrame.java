package View;
import javax.swing.*;
import java.awt.*;

//
//public class loginFrame extends JFrame {
//    public loginFrame(){
//
//        setTitle("Login Admin");
//        setSize(300, 180);
//        setDefaultCloseOperation(EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
//
//        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
//        JLabel userLabel = new JLabel("Username:");
//        JLabel passLabel = new JLabel("Password:");
//        JTextField userField = new JTextField();
//        JPasswordField passField = new JPasswordField();
//        JButton loginBtn = new JButton("Login");
//
//        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
//        panel.add(userLabel);
//        panel.add(userField);
//        panel.add(passLabel);
//        panel.add(passField);
//        panel.add(new JLabel()); // kosong
//        panel.add(loginBtn);
//
//        add(panel);
//
//        loginBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = userField.getText();
//                String password = new String(passField.getPassword());
//
//                if (username.equals("admin") && password.equals("admin")) {
//                    JOptionPane.showMessageDialog(loginFrame.this, "Login berhasil!");
//                    dispose();
//
//                    // tutup login
//                    new panelDashboard(); // buka main GUI
//                } else {
//                    JOptionPane.showMessageDialog(loginFrame.this, "Username atau Password salah!", "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//
//        setVisible(true);
//    }

//public class loginFrame extends JPanel {
//    public loginFrame(CardLayout cardLayout, JPanel parentPanel) {
//        setLayout(new GridBagLayout());
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(10, 10, 10, 10);
//
//        // Komponen login
//        JLabel lblUsername = new JLabel("Username:");
//        JTextField txtUsername = new JTextField(15);
//        JLabel lblPassword = new JLabel("Password:");
//        JPasswordField txtPassword = new JPasswordField(15);
//        JButton btnLogin = new JButton("Login");
//
//        // Action listener untuk login
//        btnLogin.addActionListener(e -> {
//            if (isValidLogin(txtUsername.getText(), new String(txtPassword.getPassword()))) {
//                cardLayout.show(parentPanel, "DASHBOARD");
//            } else {
//                JOptionPane.showMessageDialog(this, "Login gagal!");
//            }
//        });
//
//        // Tambahkan komponen ke panel
//        gbc.gridx = 0; gbc.gridy = 0;
//        add(lblUsername, gbc);
//
//        gbc.gridx = 1;
//        add(txtUsername, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 1;
//        add(lblPassword, gbc);
//
//        gbc.gridx = 1;
//        add(txtPassword, gbc);
//
//        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
//        add(btnLogin, gbc);
//    }
//
//    private boolean isValidLogin(String username, String password) {
//        // Ganti dengan validasi sesungguhnya
//        return username.equals("admin") && password.equals("admin123");
//    }
//}

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            new loginFrame().setVisible(true);
//        });
//    }


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

        // Action listener untuk login
        btnLogin.addActionListener(e -> {
            if (isValidLogin(txtUsername.getText(), new String(txtPassword.getPassword()))) {
                new panelDashboard(); // Buka frame dashboard
                dispose(); // Tutup frame login
            } else {
                JOptionPane.showMessageDialog(this, "Login gagal!");
            }
        });

        // Tambahkan komponen ke frame
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

        setVisible(true);
    }

    private boolean isValidLogin(String username, String password) {
        return username.equals("admin") && password.equals("admin123");
    }
}
