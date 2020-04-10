package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class NoBows implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (e.getRecipe().getResult().getType() == Material.BOW) {
            e.getInventory().setResult(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onBowDrop(EntitySpawnEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getEntityType() == EntityType.DROPPED_ITEM) {
                    Item item = (Item) e.getEntity();
                    if (item.getItemStack().getType() == Material.BOW) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}
