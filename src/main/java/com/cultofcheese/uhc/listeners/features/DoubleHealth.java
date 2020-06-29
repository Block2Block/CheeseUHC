package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.events.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class DoubleHealth implements Listener {

    @EventHandler
    public void onStart(GameStartEvent e) {
        for (UHCPlayer player : e.getGame().getPlayers().values()) {
            player.getPlayer().setMaxHealth(40);
            new BukkitRunnable(){
                @Override
                public void run() {
                    player.getPlayer().setHealth(40);
                }
            }.runTaskLater(UHC.get(), 1);
        }
    }

}
