package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomDrops implements Listener {

    private final Random random = new Random();
    private static final List<Material> exclusions = Arrays.asList(Material.SOIL, Material.BURNING_FURNACE, Material.MONSTER_EGG, Material.MONSTER_EGGS, Material.BARRIER, Material.BEDROCK, Material.COMMAND, Material.COMMAND_MINECART, Material.ENDER_PORTAL_FRAME, Material.DISPENSER, Material.DROPPER, Material.WRITTEN_BOOK, Material.BOOK_AND_QUILL, Material.LEAVES, Material.LEAVES_2);

    @EventHandler
    public void onItemDrop(EntitySpawnEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getEntity() instanceof Item) {
                    boolean valid = false;
                    while (!valid) {
                        Item item = (Item) e.getEntity();
                        int i = random.nextInt(Material.values().length-1);
                        Material material = Material.values()[i];
                        if (exclusions.contains(material)) {
                            continue;
                        }
                        item.getItemStack().setType(material);
                        if (material == Material.ENCHANTED_BOOK) {
                            ItemMeta im = item.getItemStack().getItemMeta();
                            int i2 = random.nextInt(Enchantment.values().length-1);
                            Enchantment enchantment = Enchantment.values()[i2];
                            im.addEnchant(enchantment, (random.nextInt(2) + 1), false);
                            item.getItemStack().setItemMeta(im);
                        }
                        valid = true;
                    }

                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(UHC.c("Game Manager","You cannot drop items while Random Drops is enabled! If you need to transfer items, use a chest!"));
    }

}
