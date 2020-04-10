package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class CraftingListener implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getRecipe().getResult().getType() == Material.GOLDEN_APPLE) {
                    if (e.getInventory().getMatrix()[4].getType() == Material.APPLE) {
                        e.getInventory().setResult(new ItemStack(Material.AIR, 1));
                    }
                }
            }
        }
    }

}
