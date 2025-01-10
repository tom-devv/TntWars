package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface GameController {

    Optional<Game> findGame(GameSettings settings);
    Game createGame(Collection<Team> teams, GameSettings settings);
    void startGame(Game game, CompletableFuture<Void> future);
    /**
     * A game can be ended forcefully or when a team runs out of lives
     * @param game
     */
    void endGame(Game game);
    void pauseGame(Game game);
    void resumeGame(Game game);
    Game getGameById(String gameId);
    /**
     * Checks if Player... is/are in a game
     * @param player player(s) to check
     * @return true if all players are in a game
     */
    boolean isInGame(Player... player);
    /**
     * Get a game via a player
     * @param player
     * @return the game if it exists
     */
    Optional<Game> getGameByPlayer(Player player);
    /**
     * Checks if two players are in the same game.
     *
     * @param player1 First player to check.
     * @param player2 Second player to check.
     * @return The shared game if both players are in the same game, otherwise empty.
     */
    Optional<Game> getSharedGame(Player player1, Player player2);
    /**
     * Respawn a player
     * @param game
     * @param player
     */
    void respawnPlayer(Game game, Player player);
    /**
     * Gracefully handle removing a player from the game
     * @param player
     */
    void removePlayer(Game game, Player player);

}
