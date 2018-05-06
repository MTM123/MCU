package me.mtm123.spigotutils.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {
    private final ItemStack i;
    private final ItemMeta imeta;
    private Material mat;
    private int amount;
    private short durability;
    
    public ItemBuilder(Material mat) {
        this.mat = mat;
        this.amount = 1;
        this.durability = 0;
        this.i = new ItemStack(mat, this.amount, this.durability);
        this.imeta = this.i.getItemMeta();
    }
    
    public ItemBuilder(int id) {
        this.mat = Material.getMaterial(id);
        this.amount = 1;
        this.durability = 0;
        this.i = new ItemStack(this.mat, this.amount, this.durability);
        this.imeta = this.i.getItemMeta();
    }
    
    public ItemBuilder() {
        this.mat = Material.STONE;
        this.amount = 1;
        this.durability = 0;
        this.i = new ItemStack(this.mat, this.amount, this.durability);
        this.imeta = this.i.getItemMeta();
    }
    
    public ItemBuilder withLore(List<String> lore) {
        lore.replaceAll(a -> ChatColor.translateAlternateColorCodes('&', a));
        imeta.setLore(lore);
        return this;
    }
    
    public ItemBuilder withLore(String... lore) {
        List<String> llore = new ArrayList<>();
        for (String l : lore) {
            llore.add(ChatColor.translateAlternateColorCodes('&', l));
        }

        imeta.setLore(llore);
        return this;
    }
    
    public ItemBuilder withMat(Material mat) {
        this.mat = mat;
        return this;
    }
    
    public ItemBuilder withMat(int id) {
        return this.withMat(Material.getMaterial(id));
    }
    
    public ItemBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }
    
    public ItemBuilder withName(String name) {
        imeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }
    
    public ItemBuilder withData(short durability) {
        this.durability = durability;
        return this;
    }
    
    public ItemBuilder withData(int durability) {
        return withData((short)durability);
    }

    public ItemBuilder setUnbreakable(boolean unbreakable){
        imeta.spigot().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment en, int level){
        imeta.addEnchant(en, level, false);
        return this;
    }

    public ItemBuilder addUnsafeEnchant(Enchantment en, int level){
        imeta.addEnchant(en, level, true);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag ... flags){
        imeta.addItemFlags(flags);
        return this;
    }

    public ItemStack build() {
        i.setAmount(this.amount);
        i.setDurability(this.durability);
        i.setType(this.mat);
        i.setItemMeta(this.imeta);
        return i;
    }
}
