package lv.mtm123.spigotutils;

import lv.mtm123.spigotutils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class InvUtil {

    private InvUtil() {}

    public static int getFreeSpaceForItem(Inventory inv, ItemStack item) {

        int freeSpace = 0;

        for (ItemStack i : inv.getContents()) {
            if (i == null || i.getType() == Material.AIR) {
                freeSpace += item.getMaxStackSize();
            } else if (i.isSimilar(item)) {
                freeSpace += item.getMaxStackSize() - i.getAmount();
            }

        }

        return freeSpace;
    }

    public static int getFreeSpaceForItem(Player player, ItemStack item) {
        return getFreeSpaceForItem(player.getInventory(), item);
    }

    public static boolean hasEnoughSpaceFor(Inventory inv, ItemStack item) {
        int spaceNeeded = item.getAmount();
        for (ItemStack i : inv.getContents()) {
            if (i == null || i.getType() == Material.AIR) {
                spaceNeeded -= item.getMaxStackSize();
            } else if (i.isSimilar(item)) {
                spaceNeeded -= i.getMaxStackSize() - i.getAmount();
            }
        }
        return spaceNeeded <= 0;
    }

    public static int countItem(Inventory inv, ItemStack item) {
        return countItem(inv.getContents(), item);
    }

    public static int countItem(ItemStack[] items, ItemStack item) {

        int count = 0;
        for (ItemStack i : items) {
            if (i != null && i.isSimilar(item))
                count += i.getAmount();
        }

        return count;

    }

    public static int countItem(Collection<ItemStack> items, ItemStack item) {

        int count = 0;
        for (ItemStack i : items) {
            if (i != null && i.isSimilar(item))
                count += i.getAmount();
        }

        return count;

    }

    public static int countItem(Inventory inv, Material mat) {

        return countItem(inv.getContents(), mat);

    }

    public static int countItem(ItemStack[] items, Material mat) {

        int count = 0;
        for (ItemStack i : items) {
            if (i != null && i.getType() == mat)
                count += i.getAmount();
        }

        return count;

    }

    public static int countItem(Collection<ItemStack> items, Material mat) {

        int count = 0;
        for (ItemStack i : items) {
            if (i != null && i.getType() == mat)
                count += i.getAmount();
        }

        return count;

    }

    public static ItemStack parseItemFromString(String idata) {
        String[] data = idata.split(" ");
        if (data.length == 0)
            return null;

        String iddata = data[0];
        String[] id = iddata.split(":");
        Material mat = Material.matchMaterial(id[0]);
        if (mat == null)
            return null;

        ItemBuilder ib = new ItemBuilder(mat);
        if (id.length == 2) {
            try {
                short durability = Short.parseShort(id[1]);
                ib.withData(durability);
            } catch (NumberFormatException e2) {
                return null;
            }
        }

        if (data.length > 1) {
            try {
                int amount = Integer.parseInt(data[1]);
                ib.withAmount(amount);
            } catch (NumberFormatException e2) {
                return null;
            }
        }

        if (data.length > 2) {

            for (int i = 2; i < data.length; ++i) {

                String di = data[i];
                if (di.startsWith("lore:")) {
                    String lorestring = di.substring(5).replace("_", " ");
                    String[] lore = lorestring.split("\\|");
                    ib.withLore(lore);
                } else if (di.startsWith("name:")) {
                    String name = di.substring(5).replace("_", " ");
                    ib.withName(name);
                } else if (di.startsWith("enchants:")) {

                    Map<Enchantment, Integer> enchantMap = new HashMap<>();
                    String enchantstring = di.substring(9);

                    String[] enchants = enchantstring.split(",");

                    for (String e : enchants) {
                        String[] ed = e.split(":");
                        if (ed.length == 2) {
                            Enchantment ench = Enchantment.getByName(ed[0].toUpperCase().replace("-", "_"));
                            if (ench != null) {
                                try {
                                    int level = Integer.parseInt(ed[1]);
                                    enchantMap.put(ench, level);
                                } catch (NumberFormatException ignored) {
                                }
                            }
                        }
                    }

                    ib.addEnchants(enchantMap);
                }

            }

        }

        return ib.build();
    }
}
