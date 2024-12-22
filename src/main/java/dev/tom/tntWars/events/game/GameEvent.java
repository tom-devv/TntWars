package dev.tom.tntWars.events.game;

import dev.tom.tntWars.events.CustomEvent;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;

public abstract class GameEvent extends CustomEvent implements Cancellable {

    private boolean cancelled = false;

    private final Game game;

    public GameEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
