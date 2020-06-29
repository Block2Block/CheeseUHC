package com.cultofcheese.uhc.listeners.features;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCParticipant;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.UHCTeam;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.entities.game.GameFeature;
import com.cultofcheese.uhc.events.GameStartEvent;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedHealth implements Listener {

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        for (UHCTeam team : e.getGame().getTeams()) {
            for (UHCPlayer player : team.getMembers()) {
                player.getPlayer().setMaxHealth(team.getMembers().size() * 20);
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        player.getPlayer().setHealth(team.getMembers().size() * 20);
                    }
                }.runTaskLater(UHC.get(), 1);
            }
        }
    }

    @EventHandler
    public void onHealthDamage(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                    if (e.getRegainReason() == EntityRegainHealthEvent.RegainReason.CUSTOM) {
                        return;
                    }
                    if (CacheManager.getGame().getConfig().isTeamed()) {
                        for (UHCPlayer member : CacheManager.getGame().getPlayers().get(player).getTeam().getMembers()) {
                            if (!member.isDead()) {
                                if (!member.getPlayer().equals(player)) {
                                    member.getPlayer().setHealth(player.getHealth() + e.getAmount());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                    if (CacheManager.getGame().getConfig().isTeamed()) {
                        for (UHCPlayer member : CacheManager.getGame().getPlayers().get(player).getTeam().getMembers()) {
                            if (!member.isDead()) {
                                if (!member.getPlayer().equals(player)) {
                                    if (e.getFinalDamage() >= member.getPlayer().getHealth()) {
                                        member.getPlayer().setMaxHealth(20);
                                        member.getPlayer().setHealth(20);
                                        Bukkit.broadcastMessage(UHC.c("Death", "&e" + member.getPlayer().getName() + "&r was killed by &e" + (WordUtils.capitalizeFully(e.getCause().name().toLowerCase(), new char[]{' '})) + "&r."));

                                        if (!CacheManager.getGame().getConfig().getFeatureEnabled(GameFeature.NO_INVENTORY_DROPS)) {
                                            PlayerInventory pi = member.getPlayer().getInventory();
                                            Location loc = member.getPlayer().getLocation();

                                            final List<ItemStack> contents = new ArrayList<>(Arrays.asList(pi.getArmorContents()));
                                            contents.addAll(Arrays.asList(pi.getContents()));

                                            ItemStack skull = new ItemStack(Material.SKULL_ITEM);
                                            skull.setDurability((short) 3);
                                            SkullMeta meta = (SkullMeta) skull.getItemMeta();
                                            meta.setOwner(player.getName());
                                            meta.setDisplayName(UHC.c(null, "&6&l" + member.getPlayer().getName() + "'s Head"));
                                            skull.setItemMeta(meta);
                                            contents.add(skull);

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
                                        }
                                        member.getPlayer().getInventory().clear();
                                        Bukkit.getWorld("uhc").strikeLightningEffect(member.getPlayer().getLocation());
                                        member.onDeath();

                                        if (CacheManager.getGame().getConfig().isTeamed()) {
                                            if (member.getTeam().aliveMembers() <= 0) {
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
                                    } else {
                                        member.getPlayer().setHealth(player.getHealth() - e.getFinalDamage());
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
