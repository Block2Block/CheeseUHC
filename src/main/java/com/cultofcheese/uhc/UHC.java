package com.cultofcheese.uhc;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class UHC extends JavaPlugin {

    private static UHC i;

    @Override
    public void onEnable() {
        i = this;
    }

    @Override
    public void onDisable() {

    }

    public static String c(String prefix, String message) {
        return ChatColor.translateAlternateColorCodes('&',((prefix != null)?"&6&l«" + prefix + "»&r ":"&r") + message);
    }
}
