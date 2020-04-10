package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

public class InstantSmelt implements Listener {

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (CacheManager.getGame().getPlayers().containsKey(e.getPlayer())) {
                    switch (e.getBlock().getType()) {
                        case GOLD_ORE:
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.GOLD_INGOT, 1));
                            break;
                        case IRON_ORE:
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.IRON_INGOT, 1));
                            break;
                        case POTATO:
                            e.setCancelled(true);
                            e.getBlock().setType(Material.AIR);
                            e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), new ItemStack(Material.BAKED_POTATO, 1));
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onKillAnimal(EntityDeathEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                Material raw = null;
                Material cooked = null;
                switch (e.getEntityType()) {
                    case COW:
                        raw = Material.RAW_BEEF;
                        cooked = Material.COOKED_BEEF;
                        break;
                    case PIG:
                        raw = Material.PORK;
                        cooked = Material.GRILLED_PORK;
                        break;
                    case SHEEP:
                        raw = Material.MUTTON;
                        cooked = Material.COOKED_MUTTON;
                        break;
                    case RABBIT:
                        raw = Material.RABBIT;
                        cooked = Material.COOKED_RABBIT;
                        break;
                    case CHICKEN:
                        raw = Material.RAW_CHICKEN;
                        cooked = Material.COOKED_CHICKEN;
                        break;
                }

                if (raw != null) {
                    for (ItemStack i : e.getDrops()) {
                        if (i.getType() == raw) {
                            i.setType(cooked);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onFish(EntitySpawnEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getEntityType() == EntityType.DROPPED_ITEM) {
                    Item item = (Item) e.getEntity();
                    if (item.getItemStack().getType() == Material.RAW_FISH) {
                        ItemStack newItem = item.getItemStack();
                        newItem.setType(Material.COOKED_FISH);
                        item.setItemStack(newItem);
                    }
                }
            }
        }
    }

}
