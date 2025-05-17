package repository;
import dashboard.kategori;
import dashboard.menu;

import java.util.ArrayList;
import java.util.List;

public class repoMenu {
    private List<menu> listMenu;
    private int id = 1;

    public repoMenu(){
        listMenu = new ArrayList<>();
    }

    public void addMenu(String nama, double harga, kategori kategori){
        listMenu.add(new menu(id++, nama, harga, kategori))
    }
}
