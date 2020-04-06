package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class StatsListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                CacheManager.getGame().getPlayers().get(e.getPlayer()).getPlayerStats().blockBreak();
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                CacheManager.getGame().getPlayers().get(e.getPlayer()).getPlayerStats().blockPlace();
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

}
