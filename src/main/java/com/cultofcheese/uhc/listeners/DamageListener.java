package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCParticipant;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.util.TitleUtil;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DamageListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                    if (!CacheManager.getGame().isPvpEnabled()) {

                        if (e.getDamager() instanceof Projectile) {
                            Projectile arrow = (Projectile) e.getDamager();
                            if (arrow.getShooter() instanceof Player) {
                                e.setCancelled(true);
                            }
                        } else if (e.getDamager() instanceof Player) {
                            e.setCancelled(true);
                        }

                    }

                    Player player = (Player) e.getEntity();

                    if (!e.isCancelled()) {
                        if (e.getFinalDamage() > player.getHealth()) {
                            e.setCancelled(true);
                            player.setHealth(20);
                            player.setMaxHealth(20);
                            Bukkit.broadcastMessage(UHC.c("Death", "&e" + player.getName() + "&r was killed by &e" + ((e.getDamager() instanceof Player)?((Player) e.getDamager()).getName():e.getDamager().getType().getName()) + "&r."));
                            if (e.getDamager() instanceof Player) {
                                ((Player) e.getDamager()).setStatistic(Statistic.PLAYER_KILLS, ((Player) e.getDamager()).getStatistic(Statistic.PLAYER_KILLS) + 1);
                            }

                            PlayerInventory pi = player.getInventory();
                            Location loc = player.getLocation();

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

                                Chest chest = (Chest) xLeft.getBlock().getState();
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        for (ItemStack item : items) {
                                            if (item == null) {
                                                continue;
                                            }
                                            chest.getBlockInventory().addItem(item);
                                        }
                                    }
                                }.runTaskAsynchronously(UHC.get());
                            }
                            player.getInventory().clear();
                            Bukkit.getWorld("uhc").strikeLightningEffect(player.getLocation());
                            CacheManager.getGame().getPlayers().get(player).onDeath();

                            if (CacheManager.getGame().getConfig().isTeamed()) {
                                if (CacheManager.getGame().getPlayers().get(player).getTeam().aliveMembers() <= 0) {
                                    List<UHCParticipant> alive = CacheManager.getGame().participantsAlive();
                                    if (alive.size() > 1) {
                                        for (Player player2 : Bukkit.getOnlinePlayers()) {
                                            TitleUtil.sendTitle(player2, CacheManager.getGame().getPlayers().get(player).getTeam().getFormattedName(), "has been eliminated from the game!", 20, 100, 20, ChatColor.WHITE, ChatColor.WHITE, true, false);
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

                        }
                    }
                } else {
                    e.setCancelled(true);
                }
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent e) {
        Bukkit.broadcastMessage("1");
        if (CacheManager.getGame() != null) {
            Bukkit.broadcastMessage("2");
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                Bukkit.broadcastMessage("3");
                if (e.getEntity() instanceof Player) {
                    if (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        return;
                    }
                    Bukkit.broadcastMessage("4");
                    Player player = (Player) e.getEntity();
                    if (!CacheManager.getSpectators().contains(player))  {
                        Bukkit.broadcastMessage("5");
                        UHCPlayer uhcPlayer = CacheManager.getGame().getPlayers().get(player);
                        if (uhcPlayer != null) {
                            Bukkit.broadcastMessage("6");
                            if (!e.isCancelled()) {
                                Bukkit.broadcastMessage("7");
                                if (e.getFinalDamage() > player.getHealth()) {
                                    Bukkit.broadcastMessage("8");
                                    e.setCancelled(true);
                                    player.setHealth(20);
                                    player.setMaxHealth(20);
                                    Bukkit.broadcastMessage(UHC.c("Death", "&e" + player.getName() + "&r was killed by &e" + (WordUtils.capitalizeFully(e.getCause().name().toLowerCase(), new char[]{' '})) + "&r."));

                                    PlayerInventory pi = player.getInventory();
                                    Location loc = player.getLocation();

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

                                        Chest chest = (Chest) xLeft.getBlock().getState();
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                for (ItemStack item : items) {
                                                    if (item == null) {
                                                        continue;
                                                    }
                                                    chest.getBlockInventory().addItem(item);
                                                }
                                            }
                                        }.runTaskAsynchronously(UHC.get());
                                    }
                                    player.getInventory().clear();
                                    Bukkit.getWorld("uhc").strikeLightningEffect(player.getLocation());
                                    CacheManager.getGame().getPlayers().get(player).onDeath();

                                    if (CacheManager.getGame().getConfig().isTeamed()) {
                                        if (CacheManager.getGame().getPlayers().get(player).getTeam().aliveMembers() <= 0) {
                                            List<UHCParticipant> alive = CacheManager.getGame().participantsAlive();
                                            if (alive.size() > 1) {
                                                for (Player player2 : Bukkit.getOnlinePlayers()) {
                                                    TitleUtil.sendTitle(player2, CacheManager.getGame().getPlayers().get(player).getTeam().getFormattedName(), "has been eliminated from the game!", 20, 100, 20, ChatColor.WHITE, ChatColor.WHITE, true, false);
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

                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
