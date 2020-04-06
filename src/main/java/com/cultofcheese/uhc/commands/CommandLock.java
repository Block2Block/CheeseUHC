package com.cultofcheese.uhc.commands;

import com.cultofcheese.uhc.UHC;
import com.cultofcheese.uhc.entities.game.Game;
import com.cultofcheese.uhc.managers.CacheManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandLock implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (CacheManager.getGame() != null) {
                if (CacheManager.getGame().getState() == Game.GameState.WAITING) {
                    if (CacheManager.getGame().getHost().getPlayer().getUniqueId().equals(player.getUniqueId())) {
                        CacheManager.getGame().lock();
                        player.sendMessage(UHC.c("Game Manager", "You have locked the server."));
                    } else {
                        player.sendMessage(UHC.c("Game Manager", "You do not have permissions to perform this command."));
                    }
                } else {
                    player.sendMessage(UHC.c("Game Manager", "The game is already locked, is full, has started or is already starting."));
                }
            } else {
                player.sendMessage(UHC.c("Game Manager","You cannot lock the game while the game is not configured."));
            }
        } else {
            sender.sendMessage("You cannot lock the game from console.");
        }
        return true;
    }
}
