package dev.tom.tntWars.events.game;

import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class GamePlayerDeathEvent extends GameEvent{

    private final Player dead;
    private final Team killers;

    /**
     * Called whenever a player dies in a game
     * @param game
     * @param dead the player who died
     * @param killers the killing team, or null if the player died to natural causes e.g falling
     */
    public GamePlayerDeathEvent(Game game, Player dead, @Nullable Team killers) {
        super(game);
        this.dead = dead;
        this.killers = killers;
    }

    public @Nullable Team getKillers() {
        return killers;
    }

    public Player getDead() {
        return dead;
    }
}
