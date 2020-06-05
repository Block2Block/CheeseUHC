package com.cultofcheese.uhc;

import com.cultofcheese.uhc.commands.*;
import com.cultofcheese.uhc.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class UHC extends JavaPlugin {

    private static UHC i;

    @Override
    public void onEnable() {
        i = this;

        registerListeners(new JoinListener(), new StatsListener(), new WorldLoadListener(), new ConfigMenuListener(), new DamageHungerListener(), new WeatherListener(), new LeaveListener(), new DamageListener(), new NetherTeleportListener(), new CraftingListener(), new MoveListener());

        getCommand("team").setExecutor(new CommandTeam());
        getCommand("start").setExecutor(new CommandStart());
        getCommand("force").setExecutor(new CommandForce());
        getCommand("lock").setExecutor(new CommandLock());
        getCommand("unlock").setExecutor(new CommandUnlock());
        getCommand("stats").setExecutor(new CommandStats());
        getCommand("teamstats").setExecutor(new CommandTeamStats());
    }

    @Override
    public void onDisable() {

    }

    /**
     * Automatically formats a plugin message with the specified prefix and message.
     *
     * This method will automatically translate alternate color codes.
     *
     * @param prefix the prefix of the message. If one is not required, set this to <code>null</code>.
     * @param message the message you want to display.
     * @return The formatted string which can be sent directly to a player without any additional formatting.
     */
    public static String c(String prefix, String message) {
        return ChatColor.translateAlternateColorCodes('&',((prefix != null)?"&6&l«" + prefix.toUpperCase() + "»&r ":"&r") + message);
    }

    /**
     * Get the instance of the plugin.
     *
     * Use for things like creating plugin tasks for the scheduler.
     *
     * @return the <code>UHC</code> instance that is currently active,
     */
    public static UHC get() {
        return i;
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }
}
