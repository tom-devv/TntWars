package dev.tom.tntWars.listeners;

import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameEventListeners implements Listener {


    @EventHandler
    public void gameStart(GameStartEvent event) {
        System.out.println("Game: " + event.getGame() + " has started");
    }

    @EventHandler
    public void gameEnd(GameEndEvent event){

    }
}
