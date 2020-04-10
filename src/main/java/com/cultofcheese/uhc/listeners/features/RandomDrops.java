package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.Random;

public class RandomDrops implements Listener {

    private Random random = new Random();

    @EventHandler
    public void onItemDrop(EntitySpawnEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getEntity() instanceof Item) {
                    Item item = (Item) e.getEntity();
                    int i = random.nextInt(Material.values().length-1);
                    Material material = Material.values()[i];
                    item.getItemStack().setType(material);
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(true);
        e.getPlayer().sendMessage(UHC.c("Game Manager","You cannot drop items while Random Drops is enabled! If you need to transfer items, use a chest!"));
    }

}
