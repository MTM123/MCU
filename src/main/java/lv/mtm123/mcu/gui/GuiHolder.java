package lv.mtm123.mcu.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

// TODO: This needs to be looked at as well
public class GuiHolder implements InventoryHolder {

    private final String title;
    private final int size;
    private final Map<Integer, Icon> icons;

    public GuiHolder(String title, int size) {
        this.title = title;
        this.size = ((size % 9 == 0) ? size : 54);
        this.icons = new HashMap<>();
    }

    public void setIcon(int slot, Icon icon) {
        this.icons.put(slot, icon);
    }

    public Icon getIcon(int slot) {
        return icons.get(slot);
    }

    public Inventory getInventory() {
        Inventory inv = Bukkit.createInventory(this, size, title);
        for (Map.Entry<Integer, Icon> e : icons.entrySet()) {
            inv.setItem(e.getKey(), e.getValue().getItem());
        }

        return inv;
    }

    public void fillEmpty(Icon icon) {
        for (int i = 0; i < this.size; ++i) {
            if (!icons.containsKey(i)) {
                setIcon(i, icon);
            }
        }
    }
}
