package com.cultofcheese.uhc.entities.game;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.listeners.features.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;

/**
 * A enumerated list of all of the different game features available to toggle.
 */
public enum GameFeature {

    INSTANT_SMELT("&7&lInstant Smelt","All food and ore automatically smelts when received.", new InstantSmelt()),
    BLOOD_DIAMONDS("&4&lBlood Diamonds", "When mining Diamonds, you take half a heart of damage.", new BloodDiamonds()),
    NO_HUNGER("&6&lNo Hunger", "Hunger is disabled.", new NoHunger()),
    NO_BOW("&6&lNo Bow", "Bows are not allowed to be crafted.", new NoBows()),
    KILL_REGEN("&6&lKill Regeneration", "Receive regeneration 1 for 5 seconds upon killing a player.", new KillRegen()),
    HOTBAR_ONLY("&6&lHotbar Only", "You can only store items in your hotbar.", new HotbarOnly()),
    DOUBLE_HEALTH("&c&lDouble Health", "Every player receives double health.", new DoubleHealth()),
    HALF_HEALTH("&c&lHalf Health", "Every player receives half health.", new HalfHealth()),
    SHARED_HEALTH("&c&lShared Health", "Each player in a team shares a single health.;&rThe team gets a combined total health of (x * 10);&rhearts where x is the team total players.", new SharedHealth()),
    NO_GAPPLES("&6&lNo Gapples", "Crafting of any Golden apples is disabled.", new NoGapples()),
    NO_ENCHANTMENTS("&7&lNo Enchantments", "All enchantments are disabled", new NoEnchantments()),
    RANDOM_DROPS("&6&lRandom Drops", "Any drops from blocks mined or mobs killed is completely random.", new RandomDrops()),
    NO_INVENTORY_DROPS("&6&lNo Inventory Drops", "Players inventory does not get dropped when they die."),
    NO_SHARING("&6&lNo Sharing", "Items dropped and put into chests are deleted and therefore players can only use what they pick up first.", new NoSharing());

    private String name,description;
    private Listener[] listeners;

    GameFeature(String name, String description, Listener... listeners) {
        this.name = name;
        this.description = description;
        this.listeners = listeners;
    }

    public Listener[] getListeners() {
        return listeners;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void registerListeners() {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, UHC.get());
        }
    }

    public static GameFeature getByName(String name) {
        for (GameFeature feature : GameFeature.values()) {
            if (ChatColor.stripColor(UHC.c(null, feature.getName())).equals(name)) {
                return feature;
            }
        }

        return null;
    }
}
