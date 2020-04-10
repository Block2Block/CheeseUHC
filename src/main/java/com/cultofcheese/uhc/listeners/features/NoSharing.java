package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.PlayerInventory;

public class NoSharing implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                e.getItemDrop().getItemStack().setType(Material.AIR);
            }
        }
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getClickedInventory() instanceof PlayerInventory) {
                    if (e.getCurrentItem() != null) {
                        if (e.getCursor() != null) {
                            if (e.getCursor().getType() == Material.AIR) {
                                if (e.getCurrentItem().getType() != Material.AIR) {
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
