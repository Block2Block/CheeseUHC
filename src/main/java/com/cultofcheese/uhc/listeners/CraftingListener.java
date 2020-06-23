package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CraftingListener implements Listener {

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e) {
        if (CacheManager.getGame() != null) {
            if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                if (e.getRecipe().getResult().getType() == Material.GOLDEN_APPLE) {
                    if (e.getInventory().getMatrix()[4].getType() == Material.APPLE) {
                        if (e.getInventory().getMatrix()[0].getType() == Material.GOLD_BLOCK) {
                            e.getInventory().setResult(new ItemStack(Material.AIR, 1));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
            Player player = (Player) e.getPlayer();
            if (player.getItemInHand() != null) {
                if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
                    if (player.getItemInHand().getDurability() == 3) {
                        //This is a skull, check if it is a golden skull.
                        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (player.getItemInHand().getItemMeta() != null) {
                                if (player.getItemInHand().getItemMeta().getDisplayName() != null) {
                                    if (player.getItemInHand().getItemMeta().getDisplayName().equals(UHC.c(null, "&6&lGolden Head"))) {
                                        ItemStack is = player.getItemInHand().clone();
                                        is.setAmount(is.getAmount() - 1);
                                        player.setItemInHand(is);
                                        player.playSound(player.getLocation(), Sound.EAT, 100, 1);
                                        player.removePotionEffect(PotionEffectType.REGENERATION);
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 2, false, true));
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
    }

}
