package com.cultofcheese.uhc.listeners;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.GameConfiguration;
import com.cultofcheese.uhc.entities.game.GameFeature;
import com.cultofcheese.uhc.entities.game.GameQuantifiedSetting;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class ConfigMenuListener implements Listener {

    @EventHandler
    public void onExit(InventoryCloseEvent e) {
        if (CacheManager.getConfig().isParsed()) {
            return;
        }
        if (e.getPlayer() instanceof Player) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (e.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING || e.getPlayer().getOpenInventory().getType() == InventoryType.CREATIVE) {
                        InventoryUtil.MainMenu((Player) e.getPlayer());
                    }
                }
            }.runTaskLater(UHC.get(), 1);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getInventory() == null) {
            return;
        }
        if (CacheManager.getGame() != null) {
            return;
        }
        if (e.getInventory().getType() == InventoryType.CHEST) {
            switch (ChatColor.stripColor(e.getInventory().getName())) {
                case "Main Configuration Menu":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    switch (e.getCurrentItem().getType()) {
                        case DIAMOND:
                            InventoryUtil.gameSettings((Player) e.getWhoClicked());
                            break;
                        case IRON_INGOT:
                            InventoryUtil.mechSettings((Player) e.getWhoClicked());
                            break;
                        case SEEDS:
                            InventoryUtil.teamSettings((Player) e.getWhoClicked());
                            break;
                        case STAINED_CLAY:
                            CacheManager.getConfig().parse(true);
                            break;
                        case REDSTONE_BLOCK:
                            ((Player) e.getWhoClicked()).kickPlayer("You cancelled setup so you were kicked from the game.");
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Game Settings Menu":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    switch (e.getSlot()) {
                        case 10:
                            InventoryUtil.changeValue(GameQuantifiedSetting.BORDER_SIZE, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 11:
                            InventoryUtil.changeValue(GameQuantifiedSetting.BORDER_TIME, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 12:
                            InventoryUtil.changeValue(GameQuantifiedSetting.BORDER_SLOW_BLOCK_SEC, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 13:
                            InventoryUtil.changeValue(GameQuantifiedSetting.BORDER_FAST_BLOCK_SEC, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 14:
                            InventoryUtil.changeValue(GameQuantifiedSetting.BORDER_SPEED_UP_DISTANCE, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 15:
                            InventoryUtil.changeValue(GameQuantifiedSetting.PVP_TIMER, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 16:
                            InventoryUtil.starterItems((Player) e.getWhoClicked());
                            break;
                        case 19:
                            InventoryUtil.changeValue(GameQuantifiedSetting.DAMAGE_TIMER, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 29:
                            InventoryUtil.changeValue(GameQuantifiedSetting.DAMAGE_AMOUNT, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Team Settings Menu":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    switch (e.getSlot()) {
                        case 10:
                            //Toggle on/off.
                            CacheManager.getConfig().setTeamed(!CacheManager.getConfig().isTeamed());
                            InventoryUtil.teamSettings((Player) e.getWhoClicked());
                            break;
                        case 12:
                            InventoryUtil.changeValue(GameQuantifiedSetting.TEAM_SIZE, ((Player) e.getWhoClicked()), e.getCurrentItem());
                            break;
                        case 14:
                            //Force fill.
                            CacheManager.getConfig().setForceFill(!CacheManager.getConfig().isForceFill());
                            InventoryUtil.teamSettings((Player) e.getWhoClicked());
                            break;
                        case 16:
                            //Random teams.
                            CacheManager.getConfig().setRandomTeams(!CacheManager.getConfig().isRandomTeams());
                            InventoryUtil.teamSettings((Player) e.getWhoClicked());
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Mechanics Settings Menu":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    switch (e.getSlot()) {
                        case 11:
                            //Nether Mechanics.
                            InventoryUtil.mechNetherSettings((Player) e.getWhoClicked());
                            break;
                        case 15:
                            //Feature mechanics.
                            InventoryUtil.mechFeaturesSettings((Player) e.getWhoClicked());
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Nether Mechanics Settings Menu":
                    switch (e.getSlot()) {
                        case 10:
                            CacheManager.getConfig().setNetherEnabled(!CacheManager.getConfig().isNetherEnabled());
                            InventoryUtil.mechNetherSettings((Player) e.getWhoClicked());
                            break;
                        case 13:
                            InventoryUtil.changeValue(GameQuantifiedSetting.NETHER_DAMAGE_TIMER, (Player) e.getWhoClicked(), e.getCurrentItem());
                            break;
                        case 16:
                            InventoryUtil.changeValue(GameQuantifiedSetting.NETHER_DAMAGE_AMOUNT, (Player) e.getWhoClicked(), e.getCurrentItem());
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Feature Mechanics Settings Menu":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    if (e.getCurrentItem().getType() == Material.STAINED_CLAY) {
                        GameFeature feature = GameFeature.getByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                        if (feature != null) {
                            CacheManager.getConfig().setGameFeature(feature, !CacheManager.getConfig().getFeatureEnabled(feature));
                            InventoryUtil.mechFeaturesSettings((Player) e.getWhoClicked());
                        }
                    } else {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Starter Items":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    if (e.getCurrentItem().getType() == Material.BARRIER) {
                        InventoryUtil.newItem((Player) e.getWhoClicked());
                    } else if (e.getCurrentItem().getType() != Material.AIR) {
                        InventoryUtil.modifyItem((Player) e.getWhoClicked(), e.getCurrentItem());
                    } else {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;

                case "Add Item":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);
                    if (e.getCurrentItem().getType() != Material.BOOK) {
                        return;
                    }

                    InventoryUtil.newItemSub((Player) e.getWhoClicked(), ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).charAt(0));
                    break;
                case "New Item":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);

                    if (e.getCurrentItem().getType() == Material.AIR) {
                        return;
                    }

                    Material material = Material.getMaterial(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    if (material != null) {
                        List<ItemStack> is = CacheManager.getConfig().getStarterItems();
                        is.add(new ItemStack(material, 1));
                        CacheManager.getConfig().setStarterItems(is);
                        InventoryUtil.starterItems((Player) e.getWhoClicked());
                    } else {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }

                    break;
                case "Modify Item":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);

                    switch (e.getSlot()) {
                        case 10:
                            //Add Enchantment
                            InventoryUtil.addEnchantment((Player) e.getWhoClicked(), e.getInventory().getItem(4));
                            break;
                        case 11:
                            //Change Amount
                            InventoryUtil.changeAmount((Player) e.getWhoClicked(), e.getInventory().getItem(4));;
                            break;
                        case 12:
                            //Change data value.
                            InventoryUtil.changeDateValue((Player) e.getWhoClicked(), e.getInventory().getItem(4));
                            break;
                        case 13:
                            //Delete the item.
                            List<ItemStack> items = CacheManager.getConfig().getStarterItems();
                            items.remove(e.getInventory().getItem(4));
                            CacheManager.getConfig().setStarterItems(items);
                            InventoryUtil.starterItems((Player) e.getWhoClicked());
                            break;
                        default:
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                case "Add Enchantment":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);

                    Enchantment enchant = Enchantment.getByName(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                    if (enchant != null) {
                        InventoryUtil.setEnchantLevel((Player) e.getWhoClicked(), e.getInventory().getItem(4), enchant);
                    } else {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Set Enchant Level":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);

                    int addAmount;
                    try {
                        addAmount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""));
                        Enchantment enchantment = Enchantment.getByName(ChatColor.stripColor(e.getInventory().getItem(13).getItemMeta().getDisplayName()));
                        if (e.getInventory().getItem(13).getEnchantmentLevel(enchantment) + addAmount < 1) {
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                            return;
                        }

                        int totalLevel = e.getInventory().getItem(13).getEnchantmentLevel(enchantment) + addAmount;
                        e.getInventory().getItem(13).removeEnchantment(enchantment);
                        e.getInventory().getItem(13).addUnsafeEnchantment(enchantment, totalLevel);
                    } catch (NumberFormatException ex) {
                        if (e.getSlot() == 22) {
                            for (ItemStack item : CacheManager.getConfig().getStarterItems()) {
                                if (item.equals(e.getInventory().getItem(4))) {
                                    item.addUnsafeEnchantments(e.getInventory().getItem(13).getEnchantments());
                                }
                            }
                            InventoryUtil.starterItems((Player) e.getWhoClicked());
                        } else {
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                        }
                    }


                    ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    break;
                case "Change Amount":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);

                    if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                        return;
                    }

                    try {
                        addAmount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""));
                        if (e.getInventory().getItem(13).getAmount() + addAmount > 64 || e.getInventory().getItem(13).getAmount() + addAmount < 1) {
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                            return;
                        }

                        for (ItemStack item : CacheManager.getConfig().getStarterItems()) {
                            if (item.equals(e.getInventory().getItem(13))) {
                                item.setAmount(e.getInventory().getItem(13).getAmount() + addAmount);
                            }
                        }
                        e.getInventory().getItem(13).setAmount(e.getInventory().getItem(13).getAmount() + addAmount);
                    } catch (NumberFormatException ex) {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                case "Change Data Value":
                    if (e.getCurrentItem() == null) {
                        return;
                    }
                    e.setCancelled(true);


                    if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                        return;
                    }
                    try {
                        addAmount = Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""));
                        if (e.getInventory().getItem(13).getDurability() + addAmount < 0 || e.getInventory().getItem(13).getDurability() + addAmount > Short.MAX_VALUE) {
                            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                            return;
                        }

                        for (ItemStack item : CacheManager.getConfig().getStarterItems()) {
                            if (item.equals(e.getInventory().getItem(13))) {
                                item.setDurability((short) (e.getInventory().getItem(13).getDurability() + addAmount));
                            }
                        }

                        e.getInventory().getItem(13).setDurability((short) (e.getInventory().getItem(13).getDurability() + addAmount));
                    } catch (NumberFormatException ex) {
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                    }
                    break;
                default:
                    GameQuantifiedSetting setting = GameQuantifiedSetting.getByName(ChatColor.stripColor(e.getInventory().getName()));

                    //Making sure they only clicked on an item that actually is part of the quantifier.
                    if (e.getCurrentItem() == null) {
                        e.setCancelled(true);
                        Player player = (Player) e.getWhoClicked();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                        return;
                    } else if (e.getCurrentItem().getType() != Material.STAINED_CLAY) {
                        e.setCancelled(true);
                        Player player = (Player) e.getWhoClicked();
                        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                        return;
                    }

                    if (setting != null) {
                        GameConfiguration config = CacheManager.getConfig();
                        switch (setting) {
                            case PVP_TIMER:
                                e.setCancelled(true);
                                String old = "" + config.getPvpTimeMinutes();

                                //Making sure this isn't going to cause any issues with the integer limit or that it doesn't go below 0.
                                if ((config.getPvpTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getPvpTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }

                                config.setPvpTimeMinutes(config.getPvpTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+","")));
                                ItemStack item = e.getInventory().getItem(13);
                                ItemMeta im = item.getItemMeta();
                                List<String> lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getPvpTimeMinutes() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case TEAM_SIZE:
                                e.setCancelled(true);
                                old = "" + config.getTeamSize();

                                //This should NEVER be over 50, as the max is set to 100. and if it were any higher, there would be 2 uneven teams.
                                if (config.getTeamSize() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+","")) > 50 || config.getTeamSize() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+","")) < 2) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }

                                config.setTeamSize((byte) (config.getTeamSize() + Byte.parseByte(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getTeamSize() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case BORDER_TIME:
                                e.setCancelled(true);
                                old = "" + config.getBorderStartTimeMinutes();

                                //Making sure this isn't going to cause any issues with the integer limit or that it doesn't go below 0.
                                if ((config.getBorderStartTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getBorderStartTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setBorderStartTimeMinutes((config.getBorderStartTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getBorderStartTimeMinutes() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case DAMAGE_TIMER:
                                e.setCancelled(true);
                                old = "" + config.getDamageTimeMinutes();

                                //Making sure this isn't going to cause any issues with the integer limit or that it doesn't go below 0.
                                if ((config.getDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setDamageTimeMinutes((config.getDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getDamageTimeMinutes() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case DAMAGE_AMOUNT:
                                e.setCancelled(true);
                                old = "" + config.getDamageAmount();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Double.MAX_VALUE / 20 / 60) || (config.getDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setDamageAmount((config.getDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getDamageAmount() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case NETHER_DAMAGE_TIMER:
                                e.setCancelled(true);
                                old = "" + config.getNetherDamageTimeMinutes();

                                //Making sure this isn't going to cause any issues with the integer limit or that it doesn't go below 0.
                                if ((config.getNetherDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getNetherDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }

                                config.setNetherDamageTimeMinutes((config.getNetherDamageTimeMinutes() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getNetherDamageTimeMinutes() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case NETHER_DAMAGE_AMOUNT:
                                e.setCancelled(true);
                                old = "" + config.getNetherDamageAmount();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getNetherDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Double.MAX_VALUE / 20 / 60) || (config.getNetherDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setNetherDamageAmount((config.getNetherDamageAmount() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getNetherDamageAmount() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case BORDER_FAST_BLOCK_SEC:
                                e.setCancelled(true);
                                old = "" + config.getBorderFastBlocksPerSecond();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getBorderFastBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Double.MAX_VALUE / 20 / 60) || (config.getBorderFastBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setBorderFastBlocksPerSecond((config.getBorderFastBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getBorderFastBlocksPerSecond() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case BORDER_SLOW_BLOCK_SEC:
                                e.setCancelled(true);
                                old = "" + config.getBorderSlowBlocksPerSecond();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getBorderSlowBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Double.MAX_VALUE / 20 / 60) || (config.getBorderSlowBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 0) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setBorderSlowBlocksPerSecond((config.getBorderSlowBlocksPerSecond() + Float.parseFloat(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getBorderSlowBlocksPerSecond() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case BORDER_SPEED_UP_DISTANCE:
                                e.setCancelled(true);
                                old = "" + config.getBorderSpeedUpDistance();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getBorderSpeedUpDistance() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getBorderSpeedUpDistance() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 32 || (config.getBorderSpeedUpDistance() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > CacheManager.getConfig().getBorderSize()) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setBorderSpeedUpDistance((config.getBorderSpeedUpDistance() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getBorderSpeedUpDistance() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                break;
                            case BORDER_SIZE:
                                e.setCancelled(true);
                                old = "" + config.getBorderSize();

                                //Making sure this isn't going to cause any issues with the double limit or it being less than 0.
                                if ((config.getBorderSize() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) > (Integer.MAX_VALUE / 20 / 60) || (config.getBorderSize() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))) < 32) {
                                    Player player = (Player) e.getWhoClicked();
                                    player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 100, 0);
                                    return;
                                }


                                config.setBorderSize((config.getBorderSize() + Integer.parseInt(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).replace("+",""))));
                                item = e.getInventory().getItem(13);
                                im = item.getItemMeta();
                                lore = new ArrayList<>();
                                for (String s : im.getLore()) {
                                    lore.add(s.replace(old, config.getBorderSize() + ""));
                                }
                                im.setLore(lore);
                                item.setItemMeta(im);
                                e.getInventory().setItem(13, item);
                                if (config.getBorderSize() < config.getBorderSpeedUpDistance()) {
                                    config.setBorderSpeedUpDistance(config.getBorderSize());
                                }
                                break;
                        }
                    } else {
                        //Somehow they've managed to open a menu that doesn't have a name that I set, so just close the menu and force them to reopen the main menu.
                        e.getWhoClicked().closeInventory();
                    }
                    break;
            }
        }
    }


}
