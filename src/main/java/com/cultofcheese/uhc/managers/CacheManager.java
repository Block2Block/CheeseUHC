package com.cultofcheese.uhc.managers;

import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.UHCTeam;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.entities.game.GameConfiguration;
import org.bukkit.entity.Player;

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
    private static Map<Player, UHCTeam> invites = new HashMap<>();
    private static List<UHCPlayer> leftPlayers = new ArrayList<>();
    private static List<Player> spectators = new ArrayList<>();

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
}
