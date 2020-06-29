package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatFormatter implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getConfig().isTeamed()) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                    if (CacheManager.getSpectators().contains(e.getPlayer())) {
                        if (CacheManager.getGame().getPlayers().containsKey(e.getPlayer())) {
                            e.setFormat(UHC.c(null, ((CacheManager.getGame().getPlayers().get(e.getPlayer()).isDead())?"&7[DEAD] ":"") + CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam().getFormattedName() + "&r&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                        } else {
                            e.setFormat(UHC.c(null, "&7" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                        }
                    } else {
                        if (CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam() != null) {
                            e.setFormat(UHC.c(null, ((CacheManager.getGame().getPlayers().get(e.getPlayer()).isDead())?"&7[DEAD] ":"") + CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam().getFormattedName() + "&r&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                        } else {
                            e.setFormat(UHC.c(null, ((CacheManager.getGame().getPlayers().get(e.getPlayer()).isDead())?"&7[DEAD] ":"") + "&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                        }
                    }
                } else {
                    if (CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam() != null) {
                        e.setFormat(UHC.c(null, CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam().getFormattedName() + "&r&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                    } else {
                        e.setFormat(UHC.c(null, "&e" + e.getPlayer().getName() + "&r: " + e.getMessage()));
                    }
                }
            } else {
                if (CacheManager.getSpectators().contains(e.getPlayer())) {
                    if (CacheManager.getGame().getPlayers().containsKey(e.getPlayer())) {
                        e.setFormat(UHC.c(null, "&7[DEAD] &e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                    } else {
                        e.setFormat(UHC.c(null, "&7" + e.getPlayer().getName() + "&r: " + e.getMessage()));
                    }
                } else {
                    if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                        e.setFormat(UHC.c(null, ((CacheManager.getGame().getPlayers().get(e.getPlayer()).isDead())?"&7[DEAD] ":"") + "&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                    } else {
                        e.setFormat(UHC.c(null, "&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
                    }
                }
            }
        } else {
            e.setFormat(UHC.c(null, "&e" + e.getPlayer().getName() + "&r: ") + e.getMessage());
        }
    }

}
