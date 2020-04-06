package com.cultofcheese.uhc.util;


import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.GameFeature;
import com.cultofcheese.uhc.entities.game.GameQuantifiedSetting;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.cultofcheese.uhc.util.ItemUtil.i;

/**
 * A util class to take lots of method calls away from the classes.
 */
public class InventoryUtil {

    /**
     * Opens the main menu of the Game Configuration for the player.
     *
     * @param player the player who should open the menu.
     */
    public static void MainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, UHC.c(null, "&6&lMain Configuration Menu"));

        border(inv);

        //First Line.
        i(inv, 10, Material.DIAMOND, "&6&lGame Settings",1, "&rChange game settings.");
        i(inv, 13, Material.SEEDS, "&6&lTeam Settings", 1, "&rChange team settings.");
        i(inv, 16, Material.IRON_INGOT, "&6&lMechanics Settings", 1, "&rChange game mechanics.");
        i(inv, 22, Material.STAINED_CLAY, "&a&lParse and Load", 1, "&rParse current game settings!;;&c&lWARNING:&r This will create the;&rgame and pre-generate all;&rneeded world chunks.", (byte) 5);
        i(inv, 31,Material.REDSTONE_BLOCK, "&6&lCancel", 1, "&rThis will kick you from;&rthe server.");

        player.openInventory(inv);
    }

    /**
     * Opens the menu containing all game-based settings that are vital to a UHC game.
     *
     * @param player the player who should open the menu.
     */
    public static void gameSettings(Player player) {
        Inventory inv = Bukkit.createInventory(null, 36, UHC.c(null, "&6&lGame Settings Menu"));
        border(inv);

        //First line
        i(inv, 10, Material.BARRIER, "&6&lBorder Size", 1, "&rBorder size: &e" + CacheManager.getConfig().getBorderSize() + ";;&r&a&lClick to change!");
        i(inv, 11, Material.WATCH, "&6&lBorder Timer", 1, "&rBorder timer: &e" + CacheManager.getConfig().getBorderStartTimeMinutes() + " minutes;;&r&a&lClick to change!");
        i(inv, 12, Material.SOUL_SAND, "&6&lBorder Slow Blocks/Sec", 1, "&rBorder Slow Block/Sec: &e" + CacheManager.getConfig().getBorderSlowBlocksPerSecond() + ";;&r&a&lClick to change!");
        i(inv, 13, Material.ICE, "&6&lBorder Fast Block/Sec", 1, "&rBorder Fast Block/Sec: &e" + CacheManager.getConfig().getBorderFastBlocksPerSecond() + ";;&r&a&lClick to change!");
        i(inv, 14, Material.BARRIER, "&6&lBorder Speed Up Distance", 1, "&rBorder speed up distance: &e" + CacheManager.getConfig().getBorderSpeedUpDistance() + ";;&r&a&lClick to change!");
        i(inv, 15, Material.DIAMOND_SWORD, "&6&lPvP Timer", 1, "&rPvP timer: &e" + CacheManager.getConfig().getPvpTimeMinutes() + " minutes;;&r&a&lClick to change!");
        i(inv, 16, Material.PAPER, "&6&lStarter Items", 1, "&r&a&lClick to change!");

        //Second line
        i(inv, 19, Material.POTION, "&6&lDamage Timer", 1, "&rDamage timer: &e" + CacheManager.getConfig().getDamageTimeMinutes() + " minutes;;&r&a&lClick to change!");
        i(inv, 20, Material.POTION, "&6&lDamage Amount", 1, "&rDamage amount: &e" + CacheManager.getConfig().getDamageAmount() + "/sec;;&r&a&lClick to change!");

        player.openInventory(inv);
    }

    /**
     * Opens the menu containing all team settings.
     *
     * @param player the player who should open the menu.
     */
    public static void teamSettings(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lTeam Settings Menu"));
        border(inv);

        //First line.
        i(inv, 10, Material.STAINED_CLAY, "&6&lEnable", 1, "&rTeams are currently: " + ((CacheManager.getConfig().isTeamed())?"&aEnabled":"&cDisabled") + ";;&r&a&lClick to toggle!", (short) 4);
        i(inv, 12, Material.SEEDS, "&6&lMax Team Size", 1, "&rMax team size: &e" + CacheManager.getConfig().getTeamSize() + ";;&r&a&lClick to change!");
        i(inv, 14, Material.STAINED_CLAY, "&6&lForce Fill", 1, "&rForce Fill is currently: " + ((CacheManager.getConfig().isForceFill())?"&aEnabled":"&cDisabled") + ";;&r&a&lClick to toggle!", (short) 14);
        i(inv, 16, Material.STAINED_CLAY, "&6&lRandom Teams", 1, "&rRandom Teams is currently: " + ((CacheManager.getConfig().isRandomTeams())?"&aEnabled":"&cDisabled") + ";;&r&a&lClick to toggle!", (short) 7);



        player.openInventory(inv);
    }

    /**
     * Opens the menu containing all settings relating to game mechanics.
     *
     * @param player the player who should open the menu.
     */
    public static void mechSettings(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lMechanics Settings Menu"));
        border(inv);

        //Line 1
        i(inv, 11, Material.NETHERRACK, "&6&lNether Mechanics", 1, "&rChange game mechanics;&rin the Nether.");
        i(inv, 15, Material.IRON_INGOT, "&6&lFeatures", 1, "&rToggle features on;&rand off.");

        //Line 2

        player.openInventory(inv);
    }

    /**
     * Opens the menu containing all settings relating to game mechanics in the nether.
     *
     * @param player the player who should open the menu.
     */
    public static void mechNetherSettings(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lNether Mechanics Settings Menu"));
        border(inv);

        //Line 1
        i(inv, 10, Material.NETHERRACK, "&6&lEnabled?", 1, "&rNether is currently: " + ((CacheManager.getConfig().isNetherEnabled())?"&aenabled":"&cdisabled") + "&r.;;&r&a&lClick to toggle!");
        i(inv, 13, Material.WATCH, "&6&lDamage Timer",1,"&rDamage timer: &e" + CacheManager.getConfig().getNetherDamageTimeMinutes() + " minutes;;&r&a&lClick to change!");
        i(inv, 16, Material.DIAMOND_SWORD, "&6&lDamage Amount", 1, "&rDamage amount: &e" + CacheManager.getConfig().getNetherDamageAmount() + "/sec;;&r&a&lClick to change!");

        player.openInventory(inv);
    }

    /**
     * Opens the menu containing all settings relating to game features. Automatically expands when more are added to the enum.
     *
     * @param player the player who should open the menu.
     */
    public static void mechFeaturesSettings(Player player) {
        int itemRows = (GameFeature.values().length / 7) + ((GameFeature.values().length % 7 > 0)?1:0);
        Inventory inv = Bukkit.createInventory(null, (itemRows + 2) * 9, UHC.c(null, "&6&lFeature Mechanics Settings Menu"));
        border(inv);

        int row = 1;
        int column = 1;
        for (GameFeature feature : GameFeature.values()) {
            i(inv, ((row * 9) + column), Material.STAINED_CLAY, feature.getName(), 1, feature.getDescription(), (byte) ((CacheManager.getConfig().getFeatureEnabled(feature))?5:14));
            if (column == 7) {
                column = 1;
                row++;
            } else {
                column++;
            }
        }

        player.openInventory(inv);
    }

    public static void changeValue(GameQuantifiedSetting setting, Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, setting.getName()));
        border(inv);
        inv.setItem(13, item);
        Object[] values = setting.getValues();

        int i = 1;
        for (Object value : values) {
            i(inv, 13+i, Material.STAINED_CLAY, "&a&l+" + value, i, "&rAdd &e" + value + "&r to the value.", (byte) 5);
            i(inv, 13-i, Material.STAINED_CLAY, "&c&l-" + value, -i, "&rRemove &e" + value + "&r to the value.", (byte) 14);
            i++;
        }

        player.openInventory(inv);

    }

    public static void starterItems(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, UHC.c(null, "&6&lStarter Items"));
        int i = 0;
        for (ItemStack item : CacheManager.getConfig().getStarterItems()) {
            inv.setItem(i, item);
            i++;
        }
        i(inv, i, Material.BARRIER, "&6&lAdd new item", 1, "&a&lClick to add new item!");
        player.openInventory(inv);
    }

    public static void newItem(Player player) {
        List<Character> chars = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m == Material.AIR || m == Material.BARRIER) {
                continue;
            }
            if (!chars.contains(m.name().charAt(0))) {
                chars.add(m.name().charAt(0));

            }
        }

        Collections.sort(chars);

        int itemRows = (chars.size() / 7) + ((chars.size() % 7 > 0)?1:0);
        Inventory inv = Bukkit.createInventory(null, (itemRows + 2) * 9, UHC.c(null, "&6&lAdd Item"));
        border(inv);

        int column = 1;
        int row = 1;
        for (char letter : chars) {
            i(inv, ((row * 9) + column), Material.BOOK, "&6&l" + letter, 1, "&rLook at all materials;&rstarting with " + letter);
            if (column == 7) {
                column = 1;
                row++;
            } else {
                column++;
            }
        }
        player.openInventory(inv);
    }

    public static void newItemSub(Player player, char searchCharacter) {
        List<Material> materials = new ArrayList<>();
        for (Material m : Material.values()) {
            if (m.name().startsWith(searchCharacter + "")) {
                if (m == Material.AIR || m == Material.BARRIER) {
                    continue;
                }
                materials.add(m);
            }
        }

        Collections.sort(materials);

        int itemRows = (materials.size() / 7) + ((materials.size() % 7 > 0)?1:0);
        Inventory inv = Bukkit.createInventory(null, (itemRows + 2) * 9, UHC.c(null, "&6&lNew Item"));
        border(inv);

        int column = 1;
        int row = 1;
        for (Material material : materials) {
            i(inv, ((row * 9) + column), material, "&6&l" + material.name(), 1, "&rAdd this item.");
            if (inv.getItem(((row * 9) + column)) == null) {
                //This means it is not a item that can be displayed in a menu.
                continue;
            } else if (inv.getItem(((row * 9) + column)).getType() != material) {
                continue;
            }
            if (column == 7) {
                column = 1;
                row++;
            } else {
                column++;
            }
        }
        player.openInventory(inv);
    }

    public static void modifyItem(Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lModify Item"));
        border(inv);
        inv.setItem(4, item);

        i(inv, 10, Material.ENCHANTED_BOOK, "&6&lAdd Enchantment", 1, "&rAdd an enchantment to;&rthe item.");
        i(inv, 11, Material.SEEDS, "&6&lIncrease Amount", 1, "&rIncrease the amount of;&rthis item given.");
        i(inv, 12, Material.BOOK, "&6&lChange Data Value", 1, "&rChange the data value;&rof the item.;;&rThis will add durability to;&ritems that have it.");
        i(inv, 13, Material.BARRIER, "&c&lDelete Item", 1, "&rDelete the item from the list");

        player.openInventory(inv);
    }

    public static void addEnchantment(Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 54, UHC.c(null, "&6&lAdd Enchantment"));
        border(inv);
        inv.setItem(4, item);

        int row = 1, column = 1;
        for (Enchantment enchant : Enchantment.values()) {
            i(inv, ((row * 9) + column), Material.ENCHANTED_BOOK, "&6&l" + enchant.getName(), 1, "&rAdd this enchantment.");
            ItemStack is = inv.getItem(((row * 9) + column));
            ItemMeta im = is.getItemMeta();
            im.addEnchant(enchant, 1, true);
            is.setItemMeta(im);
            if (column == 7) {
                column = 1;
                row++;
            } else {
                column++;
            }
        }

        player.openInventory(inv);
    }

    public static void setEnchantLevel(Player player, ItemStack item, Enchantment enchant) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lSet Enchant Level"));
        border(inv);

        inv.setItem(4, item);
        i(inv, 13, Material.ENCHANTED_BOOK, "&6&l" + enchant.getName(), 1, "&rSet this enchantment's level.");
        ItemStack is = inv.getItem(13);
        ItemMeta im = is.getItemMeta();
        im.addEnchant(enchant, 1, true);
        is.setItemMeta(im);

        int[] values = new int[]{1, 2, 5};
        int i = 1;
        for (int value : values) {
            i(inv, 13+i, Material.STAINED_CLAY, "&a&l+" + value, i, "&rAdd &e" + value + "&r to the value.", (byte) 5);
            i(inv, 13-i, Material.STAINED_CLAY, "&c&l-" + value, -i, "&rRemove &e" + value + "&r to the value.", (byte) 14);
            i++;
        }

        i(inv, 22, Material.STAINED_CLAY, "&6&lAdd", 1, "&rAdd this enchantment.", (byte) 5);

        player.openInventory(inv);
    }

    public static void changeAmount(Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lChange Amount"));
        border(inv);
        inv.setItem(13, item);

        int[] values = new int[]{1, 2, 5};
        int i = 1;
        for (int value : values) {
            i(inv, 13+i, Material.STAINED_CLAY, "&a&l+" + value, i, "&rAdd &e" + value + "&r to the value.", (byte) 5);
            i(inv, 13-i, Material.STAINED_CLAY, "&c&l-" + value, -i, "&rRemove &e" + value + "&r to the value.", (byte) 14);
            i++;
        }

        player.openInventory(inv);
    }

    public static void changeDateValue(Player player, ItemStack item) {
        Inventory inv = Bukkit.createInventory(null, 27, UHC.c(null, "&6&lChange Data Value"));
        border(inv);
        inv.setItem(13, item);

        int[] values = new int[]{1, 2, 5};
        int i = 1;
        for (int value : values) {
            i(inv, 13+i, Material.STAINED_CLAY, "&a&l+" + value, i, "&rAdd &e" + value + "&r to the value.", (byte) 5);
            i(inv, 13-i, Material.STAINED_CLAY, "&c&l-" + value, -i, "&rRemove &e" + value + "&r to the value.", (byte) 14);
            i++;
        }

        player.openInventory(inv);
    }


    public static void border(Inventory inv) {
        int size = inv.getSize();
        int rows = size / 9;
        if (size % 9 == 0 && rows >= 3) {
            for (int i = 0;i < 9;i++) {
                i(inv, i, Material.STAINED_GLASS_PANE, "&7&lPlease configure options", 1, "&rClick an option to change.;&rPress ESC to go back.", (short) 7);
            }
            for (int row = 0;row < rows - 2;row++) {
                i(inv, 9 + (row * 9), Material.STAINED_GLASS_PANE, "&7&lPlease configure options", 1, "&rClick an option to change.;&rPress ESC to go back.", (short) 7);
                i(inv, 17 + (row * 9), Material.STAINED_GLASS_PANE, "&7&lPlease configure options", 1, "&rClick an option to change.;&rPress ESC to go back.", (short) 7);
            }
            for (int i = size - 9;i < size;i++) {
                i(inv, i, Material.STAINED_GLASS_PANE, "&7&lPlease configure options", 1, "&rClick an option to change.;&rPress ESC to go back.", (short) 7);
            }
        }
    }


}
