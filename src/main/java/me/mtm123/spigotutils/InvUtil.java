package me.mtm123.spigotutils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InvUtil {

    private InvUtil(){}

    public static int getFreeSpaceForItem(Player player, ItemStack item){
        int freeSpace = 0;
        for (ItemStack i : player.getInventory().getContents()) {

            if (i == null || i.getType() == Material.AIR) {
                freeSpace += item.getMaxStackSize();
            } else if (i.isSimilar(item)) {
                freeSpace += item.getMaxStackSize() - i.getAmount();
            }
        }

        return freeSpace;
    }

    public static int countItem(Inventory inv, ItemStack item){

        int count = 0;
        for(ItemStack i : inv.getContents()){
            if(i != null && i.isSimilar(item))
                count += i.getAmount();
        }

        return count;

    }

}
