package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class NoHunger implements Listener {

    @EventHandler
    public void onHunger(FoodLevelChangeEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getEntity() instanceof Player) {
                    Player player = (Player) e.getEntity();
                    if (player.getFoodLevel() >= 20) {
                        player.setFoodLevel(30);
                        e.setCancelled(true);
                    } else if (player.getFoodLevel() < 20) {
                        player.setFoodLevel(30);
                    }
                }
            }
        }

    }

}
