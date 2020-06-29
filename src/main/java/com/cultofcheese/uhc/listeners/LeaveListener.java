package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCParticipant;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.util.TitleUtil;
import org.apache.commons.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeaveListener implements Listener {

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage(null);
        Bukkit.broadcastMessage(UHC.c("Leave", e.getPlayer().getName()));

        if (CacheManager.getGame() != null) {
            switch (CacheManager.getGame().getState()) {
                case FULL:
                    CacheManager.getGame().unlock();
                    CacheManager.getGame().playerLeave(e.getPlayer());
                    if (CacheManager.getGame().getConfig().isTeamed()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            CacheManager.getGame().getPlayers().get(player).removeFromTeam(e.getPlayer(), CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam());
                        }
                    }
                    break;
                case ACTIVE:
                    if (!CacheManager.getSpectators().contains(e.getPlayer())) {
                        if (CacheManager.getGame().isPvpEnabled()) {
                            if (!CacheManager.getGame().getPlayers().get(e.getPlayer()).isDead()) {
                                PlayerInventory pi = e.getPlayer().getInventory();
                                Location loc = e.getPlayer().getLocation();

                                final List<ItemStack> contents = new ArrayList<>(Arrays.asList(pi.getArmorContents()));
                                contents.addAll(Arrays.asList(pi.getContents()));
                                final ItemStack[] items = contents.toArray(new ItemStack[contents.size()]);

                                final double x = loc.getX();
                                final Location xLeft = loc.clone();
                                final Location xRight = loc.clone();
                                xRight.setX(x + 1);

                                Block blockLeft = xLeft.getBlock();
                                Block blockRight = xRight.getBlock();


                                if (items.length > 0) {
                                    blockLeft.setType(Material.CHEST);
                                    if (items.length > 27) {
                                        blockRight.setType(Material.CHEST);
                                    }

                                    Chest chestLeft = (Chest) xLeft.getBlock().getState();
                                    Chest chestRight = (Chest) xRight.getBlock().getState();
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            int i = 0;
                                            for (ItemStack item : items) {
                                                if (item == null) {
                                                    continue;
                                                }
                                                i++;
                                                if (i >= 28) {
                                                    chestRight.getBlockInventory().addItem(item);
                                                    continue;
                                                }
                                                chestLeft.getBlockInventory().addItem(item);
                                            }
                                        }
                                    }.runTaskAsynchronously(UHC.get());
                                }
                                e.getPlayer().getInventory().clear();
                                Bukkit.getWorld("uhc").strikeLightningEffect(e.getPlayer().getLocation());
                                CacheManager.getGame().playerLeave(e.getPlayer());

                                Bukkit.broadcastMessage(UHC.c("Death", "&e" + e.getPlayer().getName() + "&r left the game while PvP was enabled, they are now out of the game!"));

                                if (CacheManager.getGame().getConfig().isTeamed()) {
                                    if (CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam().aliveMembers() <= 0) {
                                        List<UHCParticipant> alive = CacheManager.getGame().participantsAlive();
                                        if (alive.size() > 1) {
                                            for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                TitleUtil.sendTitle(player2, CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam().getFormattedName(), "has been eliminated from the game!", 20, 100, 20, ChatColor.WHITE, ChatColor.WHITE, true, false);
                                            }
                                        } else {
                                            //A team has won.
                                            CacheManager.getGame().end(alive.get(0));
                                        }
                                    }
                                } else {
                                    List<UHCParticipant> alive = CacheManager.getGame().participantsAlive();
                                    if (alive.size() == 1) {
                                        //A player has won.
                                        CacheManager.getGame().end(alive.get(0));
                                    }
                                }
                            } else {
                                CacheManager.getSpectators().remove(e.getPlayer());
                            }
                        } else {
                            CacheManager.playerLeave(CacheManager.getGame().getPlayers().get(e.getPlayer()));
                        }
                    } else {
                        CacheManager.getSpectators().remove(e.getPlayer());
                    }
                    break;
                case ENDED:
                    break;
                default:
                    if (CacheManager.getGame().getConfig().isTeamed()) {
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            CacheManager.getGame().getPlayers().get(player).removeFromTeam(e.getPlayer(), CacheManager.getGame().getPlayers().get(e.getPlayer()).getTeam());
                        }
                    }
                    CacheManager.getGame().playerLeave(e.getPlayer());
                    break;
            }
        }
    }

}
