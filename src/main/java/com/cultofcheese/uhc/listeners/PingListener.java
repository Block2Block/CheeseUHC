package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

    @EventHandler
    public void onPlayerPing(ServerListPingEvent e) {
        StringBuilder sb = new StringBuilder("\u00A76\u00A7l            « Cult of Cheese UHC »\n");
        if (CacheManager.getGame() == null) {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                //The host is online, disallow until the game is ready to play.
                sb.append("  \u00A7eThe host is currently configuring the game.");
            } else {
                sb.append("      \u00A7eJoin to configure and host the game!");
            }
        } else {
            if (!CacheManager.getGame().isGenerated()) {
                //Map is still being generated.
                sb.append("   \u00A7eThe map is being generated. Progress: ").append(CacheManager.getGame().getPercentage()).append("%");
            } else if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                sb.append("          \u00A7eThe game has begun! Join to spectate!");
            } else {
                if (Bukkit.getOnlinePlayers().size() >= CacheManager.getGame().getMAX()) {
                    sb.append("             \u00A7eThe game is now full!");
                } else if (CacheManager.getGame().isLocked()) {
                    sb.append("     \u00A7eThe game has been locked by the host!");
                }  else if (CacheManager.getGame().getState() == Game.GameState.ENDED) {
                    sb.append("  \u00A7eThe game has now ended. Thanks for playing!");
                } else {
                    sb.append("          \u00A7eThe game is open! Join now!");
                }
            }
        }

        e.setMotd(sb.toString());
    }

}
