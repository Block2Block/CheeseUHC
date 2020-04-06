package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import com.cultofcheese.uhc.util.TitleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandForce implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.ACTIVE) {
                    if (CacheManager.getGame().getHost().getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        if (args.length == 1) {
                            switch (args[0].toLowerCase()) {
                                case "borderstart":
                                    if (CacheManager.getGame().getBorderState() == Game.BorderState.INACTIVE) {
                                        CacheManager.getGame().startBorder();
                                        player.sendMessage(UHC.c("Game Manager","You forced the border to start moving."));
                                    } else {
                                        player.sendMessage(UHC.c("Game Manager","The border is either is already moving, has already moved or is disabled."));
                                    }
                                    break;
                                case "borderspeedup":
                                    if (CacheManager.getGame().getBorderState() == Game.BorderState.MOVING) {
                                        CacheManager.getGame().startBorderSpeedUp();
                                        player.sendMessage(UHC.c("Game Manager","You forced the border to speed up."));
                                    } else {
                                        player.sendMessage(UHC.c("Game Manager","The border must be moving in its slow state to force it to speed up."));
                                    }
                                    break;
                                case "pvp":
                                    if (!CacheManager.getGame().isPvpEnabled()) {
                                        CacheManager.getGame().startPvP();
                                        player.sendMessage(UHC.c("Game Manager","You forced PvP to be enabled."));

                                    } else {
                                        player.sendMessage(UHC.c("Game Manager", "PvP is already enabled."));
                                    }
                                    break;
                                case "damage":
                                    if (!CacheManager.getGame().isDamageActive()) {
                                        CacheManager.getGame().startDamage();
                                        player.sendMessage(UHC.c("Game Manager","You forced damage to start."));
                                    } else {
                                        player.sendMessage(UHC.c("Game Manager", "Damage is already active."));
                                    }
                                    break;
                                default:
                                    player.sendMessage(UHC.c("Game Manager", "Available events:\n" +
                                            "&eBorderStart&r - Starts the Border movement\n" +
                                            "&eBorderSpeedUp&r - Starts the border speed up\n" +
                                            "&ePvP&r - Enabled PvP\n" +
                                            "&eDamage&r - Starts dealing damage to the players."));
                            }
                        } else {
                            player.sendMessage(UHC.c("Game Manager", "Available events:\n" +
                                    "&eBorderStart&r - Starts the Border movement\n" +
                                    "&eBorderSpeedUp&r - Starts the border speed up\n" +
                                    "&ePvP&r - Enabled PvP\n" +
                                    "&eDamage&r - Starts dealing damage to the players."));
                        }
                    } else {
                        player.sendMessage(UHC.c("Game Manager", "You do not have permissions to perform this command."));
                    }
                } else {
                    player.sendMessage(UHC.c("Game Manager", "You can only force events once the game is active."));
                }
            } else {
                player.sendMessage(UHC.c("Game Manager","You cannot unlock the game while the game is not configured."));
            }
        } else {
            sender.sendMessage("You cannot start the game from console.");
        }
        return true;
    }
}
