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

public class CommandTeamStats implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE || CacheManager.getGame().getState() == Game.GameState.ENDED) {
                    UHCPlayer uhcPlayer = CacheManager.getGame().getPlayers().get(player);
                    if (uhcPlayer != null) {
                        if (CacheManager.getGame().getConfig().isTeamed()) {
                            int kills = 0, blocksBroken = 0, blocksPlaced = 0;
                            double blocksTravelled = 0, damageDealt = 0, damageTaken = 0;
                            for  (UHCPlayer member : uhcPlayer.getTeam().getMembers()) {
                                kills += member.getPlayerStats().getKills();
                                blocksTravelled += member.getPlayerStats().getDistanceTravelled();
                                damageDealt += member.getPlayerStats().getDamageDealt();
                                damageTaken += member.getPlayerStats().getDamageTaken();
                                blocksBroken += member.getPlayerStats().getBlocksBroken();
                                blocksPlaced += member.getPlayerStats().getBlocksPlaced();
                            }

                            player.sendMessage(UHC.c("Stats Manager", "Your current combined team game statistics:\n" +
                                    "Kills: &e" + kills + "&r\n" +
                                    "Blocks Travelled: &e" + blocksTravelled + "&r\n" +
                                    "Damage Dealt: &e" + damageDealt + "&r\n" +
                                    "Damage Taken: &e" + damageTaken + "&r\n" +
                                    "Blocks Broken: &e" + blocksBroken + "&r\n" +
                                    "Blocks Placed: &e" + blocksPlaced));
                        } else {
                            player.sendMessage(UHC.c("Stats Manager","The game is not in teamed mode so there are no team statistics to display."));
                        }
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
