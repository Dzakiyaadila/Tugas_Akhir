package CRUD;

import Logic.PaymentSettings;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class repoPaymentSettings {
    private Map<String, PaymentSettings> settingsMap;
    private static final String PAYMENT_SETTINGS_FILE = "data/payment_settings.csv";

    public repoPaymentSettings() {
        settingsMap = new HashMap<>();
        new File("data").mkdirs();
        loadSettingsFromFile();

        if (settingsMap.isEmpty()) {
            addOrUpdateSetting("Cash", true); // Default aktif
            addOrUpdateSetting("QRIS", true); // Default aktif
            saveSettingsToFile(); // Simpan default ke file
        }
    }

    public boolean isMethodActive(String methodName) {
        PaymentSettings setting = settingsMap.get(methodName);
        return setting != null && setting.isActive();
    }

    public void addOrUpdateSetting(String methodName, boolean isActive) {
        PaymentSettings setting = settingsMap.get(methodName);
        if (setting == null) {
            setting = new PaymentSettings(methodName, isActive);
            settingsMap.put(methodName, setting);
        } else {
            setting.setActive(isActive);
        }
        saveSettingsToFile(); // Simpan setiap kali ada perubahan
    }

    private void loadSettingsFromFile() {
        settingsMap.clear();
        File file = new File(PAYMENT_SETTINGS_FILE);
        if (!file.exists() || file.length() == 0) {
            System.out.println("File CSV pengaturan pembayaran tidak ditemukan atau kosong.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_SETTINGS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PaymentSettings setting = PaymentSettings.fromCsvString(line);
                if (setting != null) {
                    settingsMap.put(setting.getMethodName(), setting);
                } else {
                    System.err.println("Skipping malformed payment setting row: " + line);
                }
            }
            System.out.println("Pengaturan pembayaran berhasil dimuat dari " + PAYMENT_SETTINGS_FILE + ". Jumlah: " + settingsMap.size());
        } catch (IOException e) {
            System.err.println("Error loading payment settings from CSV: " + e.getMessage());
        }
    }

    private void saveSettingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_SETTINGS_FILE))) {
            for (PaymentSettings setting : settingsMap.values()) {
                writer.write(setting.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving payment settings to CSV: " + e.getMessage());
        }
    }
}