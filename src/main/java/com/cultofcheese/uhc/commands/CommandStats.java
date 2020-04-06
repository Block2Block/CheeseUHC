package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.PlayerStats;
import com.cultofcheese.uhc.entities.UHCPlayer;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE || CacheManager.getGame().getState() == Game.GameState.ENDED) {
                    UHCPlayer uhcPlayer = CacheManager.getGame().getPlayers().get(player);
                    if (uhcPlayer != null) {
                        PlayerStats stats = uhcPlayer.getPlayerStats();
                        player.sendMessage(UHC.c("Stats Manager", "Your current game statistics:\n" +
                                "Kills: &e" + stats.getKills() + "&r\n" +
                                "Blocks Travelled: &e" + stats.getDistanceTravelled() + "&r\n" +
                                "Damage Dealt: &e" + stats.getDamageDealt() + "&r\n" +
                                "Damage Taken: &e" + stats.getDamageTaken() + "&r\n" +
                                "Blocks Broken: &e" + stats.getBlocksBroken() + "&r\n" +
                                "Blocks Placed: &e" + stats.getBlocksPlaced()));
                    } else {
                        player.sendMessage(UHC.c("Stats Manager", "You did not participate in the game so you do not have any game stats."));
                    }
                } else {
                    player.sendMessage(UHC.c("Stats Manager", "You cannot get stats as there are no stats to show."));
                }
            } else {
                player.sendMessage(UHC.c("Stats Manager", "You cannot get stats when the game is not configured."));
            }
        }
        return true;
    }
}
