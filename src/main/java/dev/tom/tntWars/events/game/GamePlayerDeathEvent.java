package dev.tom.tntWars.events.game;

import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;

public class GamePlayerDeathEvent extends GameEvent{

    private final Player dead;

    /**
     * Called whenever a player dies in a game
     * @param game
     * @param dead the player who died
     */
    public GamePlayerDeathEvent(Game game, Player dead) {
        super(game);
        this.dead = dead;
    }

    public Player getDead() {
        return dead;
    }
}
