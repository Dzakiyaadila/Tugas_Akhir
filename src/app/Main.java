package app;
import View.loginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new loginFrame(); // Langsung buka dashboard
        });
    }
}