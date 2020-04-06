package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUnlock implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.USER_LOCK) {
                    if (CacheManager.getGame().getHost().getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        CacheManager.getGame().unlock();
                        player.sendMessage(UHC.c("Game Manager", "You have unlocked the server."));
                    } else {
                        player.sendMessage(UHC.c("Game Manager", "You do not have permissions to perform this command."));
                    }
                } else {
                    player.sendMessage(UHC.c("Game Manager", "The game is already unlocked, is full, has started or is already starting."));
                }
            } else {
                player.sendMessage(UHC.c("Game Manager","You cannot unlock the game while the game is not configured."));
            }
        } else {
            sender.sendMessage("You cannot unlock the game from console.");
        }
        return true;
    }
}
