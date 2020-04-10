package com.cultofcheese.uhc.listeners.features;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

public class NoEnchantments implements Listener {

    @EventHandler
    public void onEnchant(PrepareItemEnchantEvent e) {
        e.setCancelled(true);
    }

}
