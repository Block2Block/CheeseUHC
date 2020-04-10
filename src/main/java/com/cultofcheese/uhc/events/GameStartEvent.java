package com.cultofcheese.uhc.events;

import com.cultofcheese.uhc.entities.game.Game;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStartEvent extends Event {

    private static HandlerList handerList = new HandlerList();
    private Game game;

    public GameStartEvent(Game game) {
        this.game = game;
    }

    @Override
    public HandlerList getHandlers() {
        return handerList;
    }

    public static HandlerList getHandlerList() {
        return handerList;
    }

    public Game getGame() {
        return game;
    }
}
