package Logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class PaymentSettings {
    private String methodName;
    private boolean isActive;

    public PaymentSettings(String methodName, boolean isActive) {
        this.methodName = methodName;
        this.isActive = isActive;
    }

    public String getMethodName() { return methodName; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public String toCsvString() {
        return methodName + "," + isActive;
    }

    public static PaymentSettings fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 2) {
            try {
                String methodName = parts[0].trim();
                boolean isActive = Boolean.parseBoolean(parts[1].trim());
                return new PaymentSettings(methodName, isActive);
            } catch (Exception e) {
                System.err.println("Error parsing PaymentSettings from CSV: " + csvLine + " - " + e.getMessage());
            }
        } else {
            System.err.println("Invalid CSV format for PaymentSettings: " + csvLine);
        }
        return null;
    }
}