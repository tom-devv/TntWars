package dev.tom.tntWars.events.game;

import dev.tom.tntWars.models.game.Game;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends GameEvent {

    public GameEndEvent(Game game) {
        super(game);
    }


}
