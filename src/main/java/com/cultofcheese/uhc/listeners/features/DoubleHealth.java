package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.events.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DoubleHealth implements Listener {

    @EventHandler
    public void onStart(GameStartEvent e) {
        for (UHCPlayer player : e.getGame().getPlayers().values()) {
            player.getPlayer().setMaxHealth(40);
            player.getPlayer().setHealth(40);
        }
    }

}
