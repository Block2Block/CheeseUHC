package com.cultofcheese.uhc.managers;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.UHCTeam;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.entities.game.GameConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Small manager class to deal with anything needed to be stored indefinitely.
 */
public class CacheManager {

    private static int idCounter = 0;
    private static Game game;
    private static GameConfiguration config;
    final private static Map<Player, UHCTeam> invites = new HashMap<>();
    final private static List<UHCPlayer> leftPlayers = new ArrayList<>();
    final private static List<Player> spectators = new ArrayList<>();
    final private static Map<Player, BukkitTask> netherPlayers = new HashMap<>();

    public static Game getGame() {
        return game;
    }

    public static void setGame(Game newGame) {
        game = newGame;
    }

    public static GameConfiguration getConfig() {
        return config;
    }

    public static void newConfig(Player host) {
        config = new GameConfiguration(host);
    }

    public static Map<Player, UHCTeam> getInvites() {
        return invites;
    }

    public static int newTeam() {
        return idCounter++;
    }

    public static void playerLeave(UHCPlayer player) {
        leftPlayers.add(player);
    }

    public static void playerRejoin(UHCPlayer player) {
        leftPlayers.remove(player);
    }

    public static List<UHCPlayer> getLeftPlayers() {
        return new ArrayList<>(leftPlayers);
    }

    public static List<Player> getSpectators() {
        return spectators;
    }

    public static void enterNether(Player player) {
        if (game.getConfig().getNetherDamageTimeMinutes() > 0 && game.getConfig().getNetherDamageAmount() > 0) {
            netherPlayers.put(player, new BukkitRunnable(){
                @Override
                public void run() {
                    player.damage(game.getConfig().getNetherDamageAmount());
                }
            }.runTaskTimer(UHC.get(), game.getConfig().getNetherDamageTimeMinutes() * 1200, 20));
        }
    }

    public static void leaveNether(Player player) {
        if (game.getConfig().getNetherDamageTimeMinutes() > 0 && game.getConfig().getNetherDamageAmount() > 0) {
            BukkitTask task = netherPlayers.remove(player);
            if (task != null) {
                task.cancel();
            }
        }
    }
}
