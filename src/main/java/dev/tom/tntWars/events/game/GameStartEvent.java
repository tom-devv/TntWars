package dev.tom.tntWars.events.game;

import dev.tom.tntWars.events.CustomEvent;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameStartEvent extends GameEvent  {

    public GameStartEvent(Game game) {
        super(game);
    }

}
