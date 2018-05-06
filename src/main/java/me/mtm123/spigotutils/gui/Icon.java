package me.mtm123.spigotutils.gui;

import org.bukkit.inventory.ItemStack;

public class Icon
{
    private final ItemStack item;
    private final ClickAction action;
    
    public Icon(final ItemStack item, final ClickAction action) {
        this.item = item;
        this.action = action;
    }
    
    public ClickAction getAction() {
        return action;
    }
    
    public ItemStack getItem() {
        return item;
    }
}
