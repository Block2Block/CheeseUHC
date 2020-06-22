package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.events.GameStartEvent;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static com.cultofcheese.uhc.util.ItemUtil.i;

public class HotbarOnly implements Listener {

    @EventHandler
    public void onItemPickup(InventoryClickEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getCurrentItem() == null) {
                    return;
                }
                if (e.getCurrentItem().getType() == Material.BARRIER) {
                    e.setCancelled(true);
                    Player player = (Player) e.getWhoClicked();
                    player.playSound(player.getLocation(), Sound.NOTE_BASS, 100, 1);
                }
            } else {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        for (UHCPlayer player : e.getGame().getPlayers().values()) {
            for (int i = 9;i < 36;i++) {
                i(player.getPlayer().getInventory(), i, Material.BARRIER, "&c&lThis slot is disabled!", 1, "&rYou may only use your Hotbar!");
            }
        }
    }

}
