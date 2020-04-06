package com.cultofcheese.uhc.util;

import com.cultofcheese.uhc.UHC;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

/**
 * Util class for making it easier to create <code>ItemStack</code> objects for GUIs.
 */
public class ItemUtil {

    public static void i(Inventory inv, int slot, Material type, String name, int amount, String lore, short data, boolean glowing, String skullName, Color color) {
        ItemStack is = new ItemStack(type, amount, data);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(UHC.c(null, name));
        if (lore != null) {
            im.setLore(Arrays.asList(UHC.c(null, lore).split(";")));
        }
        if (glowing) {
            im.addEnchant(Enchantment.DURABILITY, 1, true);
            im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if (skullName != null) {
            SkullMeta sm = (SkullMeta) im;
            sm.setOwner(skullName);
            im = sm;
        }
        if (color != null) {
            LeatherArmorMeta am = (LeatherArmorMeta) im;
            am.setColor(color);
            im = am;
        }
        is.setItemMeta(im);
        inv.setItem(slot, is);
    }
    public static void i(Inventory inv, int slot, Material type, String name, int amount, String lore, short data, boolean glowing, String skullName) {
        i(inv, slot, type, name, amount, lore, data, glowing, skullName, null);
    }
    public static void i(Inventory inv, int slot, Material type, String name, int amount, String lore, short data, boolean glowing) {
        i(inv, slot, type, name, amount, lore, data, glowing, null);
    }
    public static void i(Inventory inv, int slot, Material type, String name, int amount, String lore, short data) {
        i(inv, slot, type, name, amount, lore, data, false);
    }
    public static void i(Inventory inv, int slot, Material type, String name, int amount, String lore) {
        i(inv, slot, type, name, amount, lore, (short)0);
    }
    public static void i(Inventory inv, int slot, Material type, String name, int amount) {
        i(inv, slot, type, name, amount, null);
    }
    public static void i(Inventory inv, int slot, Material type, String name) {
        i(inv, slot, type, name, 1);
    }
    public static void i(Inventory inv, int slot, Material type) {
        i(inv, slot, type, "");
    }

}
