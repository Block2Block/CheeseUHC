package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.STARTING) {
                if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                    e.setTo(e.getFrom());
                }
            }
        }
    }

}
