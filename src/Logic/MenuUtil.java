// File: Logic/MenuUtil.java
package Logic;

import java.io.*;
import java.util.*;

public class MenuUtil {
    public static List<MenuItem> loadMenuFromCSV(String filePath) {
        List<MenuItem> menuList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if(data.length >= 5) {
                    menuList.add(new MenuItem(
                            data[0], // nama
                            data[1], // kategori
                            Integer.parseInt(data[2]), // harga
                            Integer.parseInt(data[3]), // stok
                            data[4] // supplier
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuList;
    }
}
