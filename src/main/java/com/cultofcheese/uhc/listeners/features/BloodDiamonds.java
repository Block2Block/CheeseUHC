package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BloodDiamonds implements Listener {

    @EventHandler
    public void onMine(BlockBreakEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getBlock().getType() == Material.DIAMOND_ORE) {
                    e.getPlayer().damage(1);
                }
            }
        }
    }

}
