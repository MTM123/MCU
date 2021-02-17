package lv.mtm123.mcu.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ItemUtil {

    public static int getFreeSpaceForItem(Player player, ItemStack item) {
        int freeSpace = 0;
        for (ItemStack i : player.getInventory().getContents()) {

            if (i == null || i.getType().equals(Material.AIR)) {
                freeSpace += item.getMaxStackSize();
            } else if (i.isSimilar(item)) {
                freeSpace += item.getMaxStackSize() - i.getAmount();
            }
        }

        return freeSpace;
    }

    public static int getFreeSpaceNoAir(Player player, ItemStack item) {
        int freeSpace = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i.isSimilar(item)) {
                freeSpace += item.getMaxStackSize() - i.getAmount();
            }
        }

        return freeSpace;
    }

    public static int getAirSlots(Player player) {
        int airSlots = 0;
        for (ItemStack i : player.getInventory().getContents()) {
            if (i == null || i.getType().equals(Material.AIR))
                airSlots++;
        }

        return airSlots;
    }

}
