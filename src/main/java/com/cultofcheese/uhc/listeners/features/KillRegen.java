package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class KillRegen implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onKill(EntityDamageByEntityEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                    if (CacheManager.getGame().isPvpEnabled()) {
                        Player player = (Player) e.getEntity();
                        Player damager = (Player) e.getDamager();

                        if (player.getGameMode() == GameMode.SPECTATOR || e.getFinalDamage() >= player.getHealth()) {
                            damager.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
                        }
                    }
                }
            }
        }
    }

}
