package View;
import Logic.menu;

import java.util.List;

public interface MenuDataChangeListener {
    void onMenuDataChanged(List<menu> updateMenuList);
}
