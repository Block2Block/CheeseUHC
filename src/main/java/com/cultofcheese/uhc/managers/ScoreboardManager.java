package com.cultofcheese.uhc.managers;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.UHCPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;

import java.util.Set;

public class ScoreboardManager {

    public static void changeLinePlayer(Player player, int line, String message) {
        resetLinePlayer(player, line);

        Score score = CacheManager.getGame().getPlayers().get(player).getObjective().getScore(UHC.c(null, message));
        score.setScore(line);
    }

    public static void changeLineGlobal(int line, String message) {
        for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
            Player player = uhcPlayer.getPlayer();
            changeLinePlayer(player, line, message);
        }
    }

    public static void resetLinePlayer(Player player, int line) {
        Set<String> entries = CacheManager.getGame().getPlayers().get(player).getScoreboard().getEntries();
        for (String entry : entries) {
            if (CacheManager.getGame().getPlayers().get(player).getObjective().getScore(entry).getScore() == line) {
                CacheManager.getGame().getPlayers().get(player).getScoreboard().resetScores(entry);
            }
        }
    }

    public static void resetLineGame(int line) {
        for (UHCPlayer uhcPlayer : CacheManager.getGame().getPlayers().values()) {
            Player player = uhcPlayer.getPlayer();
            resetLinePlayer(player, line);
        }
    }


}
