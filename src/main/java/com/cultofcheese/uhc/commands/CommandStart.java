package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandStart implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.WAITING || CacheManager.getGame().getState() == Game.GameState.LOCKED || CacheManager.getGame().getState() == Game.GameState.FULL ||  CacheManager.getGame().getState() == Game.GameState.USER_LOCK) {
                    if (CacheManager.getGame().getHost().getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        if (CacheManager.getGame().getConfig().isTeamed()) {
                            if (CacheManager.getGame().getTeams().size() <= 1) {
                                if (CacheManager.getGame().getPlayers().size() <= CacheManager.getGame().getConfig().getTeamSize()) {
                                    player.sendMessage(UHC.c("Game Manager", "You cannot start the game with less than enough players to create at least 2 teams."));
                                    return true;
                                }
                            }
                        } else {
                            if (CacheManager.getGame().getPlayers().size() <= 1) {
                                player.sendMessage(UHC.c("Game Manager", "You cannot start the game with less than 2 players."));
                                return true;
                            }
                        }
                        if (args.length == 1) {
                            int i = -1;
                            try {
                                i = Integer.parseInt(args[0]);
                            } catch (NumberFormatException e) {
                                player.sendMessage(UHC.c("Game Manager", "That is an invalid time."));
                                return true;
                            }

                            if (i < 1) {
                                player.sendMessage(UHC.c("Game Manager", "That is an invalid time."));
                                return true;
                            }

                            CacheManager.getGame().startGame(i);
                            player.sendMessage(UHC.c("Game Manager", "The game will start in &e" + i + " &rseconds"));
                        } else if (args.length > 1) {
                            player.sendMessage(UHC.c("Game Manager", "Invalid syntax. Correct syntax: &e/start [time in seconds]"));
                        } else {
                            CacheManager.getGame().startGame(60);
                        }
                    } else {
                        player.sendMessage(UHC.c("Game Manager", "You do not have permissions to perform this command."));
                    }
                } else {
                    player.sendMessage(UHC.c("Game Manager", "The game has started or is already starting."));
                }
            } else {
                player.sendMessage(UHC.c("Game Manager","You cannot start the game while the game is not configured."));
            }
        } else {
            sender.sendMessage("You cannot start the game from console.");
        }
        return true;
    }
}
