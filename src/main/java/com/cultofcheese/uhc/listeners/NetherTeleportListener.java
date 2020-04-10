package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NetherTeleportListener implements Listener {

    @EventHandler
    public void onPortal(PlayerPortalEvent e) {
        Player player = e.getPlayer();

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if (e.getFrom().getWorld().getName().equals("uhc")) {
                if (CacheManager.getGame().getConfig().isNetherEnabled()) {
                    e.useTravelAgent(true);
                    e.getPortalTravelAgent().setCanCreatePortal(true);

                    Location l = e.getFrom();
                    l.setX(l.getBlockX() / 8d);
                    l.setZ(l.getBlockZ() / 8d);
                    l.setWorld(Bukkit.getWorld("uhc_nether"));

                    e.setTo(e.getPortalTravelAgent().findOrCreate(l));
                    CacheManager.enterNether(player);
                } else {
                    player.sendMessage(UHC.c("Game Manager", "The Nether is disabled in this event!"));
                    e.setCancelled(true);
                }
            } else if (e.getFrom().getWorld().getName().equals("uhc_nether")) {
                e.useTravelAgent(true);
                e.getPortalTravelAgent().setCanCreatePortal(true);

                Location l = e.getFrom();
                l.setX(l.getBlockX() * 8d);
                l.setZ(l.getBlockZ() * 8d);
                l.setWorld(Bukkit.getWorld("uhc"));

                e.setTo(e.getPortalTravelAgent().findOrCreate(l));
                CacheManager.leaveNether(player);
            } else {
                e.setCancelled(true);
            }
        } else if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            e.setCancelled(true);
        }
    }

}
