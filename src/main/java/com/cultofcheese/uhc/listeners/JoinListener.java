package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * This class deals with the listening events needed to stop players from joining once the game has started.
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Bukkit.broadcastMessage(UHC.c("Join", e.getPlayer().getName()));

        if (CacheManager.getGame() == null) {
            //This is the host, they needs to configure the game. Open the game menu.
            CacheManager.newConfig(e.getPlayer());
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
            e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 0, 100, 0));
            e.getPlayer().getInventory().clear();
            e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1)});
            e.getPlayer().setMaxHealth(20);
            e.getPlayer().setHealth(20);
            new BukkitRunnable(){
                @Override
                public void run() {
                    InventoryUtil.MainMenu(e.getPlayer());
                }
            }.runTaskLater(UHC.get(), 1);
        } else {
            //This is not the host and the game is either active or waiting and not full, join the game.
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                UHCPlayer uhcPlayer = null;
                for (UHCPlayer player : CacheManager.getLeftPlayers()) {
                    if (player.getPlayer().getUniqueId().equals(e.getPlayer().getUniqueId())) {
                        uhcPlayer = player;
                        player.getPlayer().sendMessage(UHC.c("Game Manager", "Because you reconnected before PvP was enabled, you have been spawned back into the game."));
                        break;
                    }
                }
                if (uhcPlayer == null) {
                    e.getPlayer().setGameMode(GameMode.SPECTATOR);
                    e.getPlayer().getInventory().clear();
                    e.getPlayer().sendMessage(UHC.c("Game Manager", "You are a spectator in the match."));
                    e.getPlayer().teleport(new Location(Bukkit.getWorld("uhc"), 0, Bukkit.getWorld("uhc").getHighestBlockYAt(0, 0), 0));
                    CacheManager.getSpectators().add(e.getPlayer());
                } else {
                    CacheManager.playerRejoin(uhcPlayer);
                }
            } else {
                CacheManager.getGame().onJoin(e.getPlayer());
                e.getPlayer().setGameMode(GameMode.SURVIVAL);
                e.getPlayer().teleport(new Location(Bukkit.getWorlds().get(0), 0, 100, 0));
                e.getPlayer().getInventory().clear();
                e.getPlayer().getInventory().setArmorContents(new ItemStack[]{new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1),new ItemStack(Material.AIR, 1)});
                e.getPlayer().setMaxHealth(20);
                e.getPlayer().setHealth(20);
            }

        }
        e.getPlayer().setFoodLevel(30);
        e.getPlayer().setHealth(20);
    }

    @EventHandler
    public void preJoin(AsyncPlayerPreLoginEvent e) {
        if (CacheManager.getGame() == null) {
            if (Bukkit.getOnlinePlayers().size() > 0) {
                //The host is online, disallow until the game is ready to play.
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, UHC.c("UHC","The host is currently configuring the game. Please wait."));
            }
        } else {
            if (!CacheManager.getGame().isGenerated()) {
                //Map is still being generated.
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, UHC.c("UHC","The map is currently being generated. In order to prevent lag, you cannot join while chunk generation is in progress.\n" +
                        "Please try again later."));
            } else if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                e.allow();
            } else {
                if (Bukkit.getOnlinePlayers().size() >= CacheManager.getGame().getMAX()) {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, UHC.c("UHC","This UHC Event is now full.\n" +
                            "Please try again later."));
                } else if (CacheManager.getGame().isLocked()) {
                    e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, UHC.c("UHC","This UHC Event is now locked and cannot be joined for new players.\n" +
                            "Please try again later."));
                } else {
                    e.allow();
                }
            }
        }
    }

}
