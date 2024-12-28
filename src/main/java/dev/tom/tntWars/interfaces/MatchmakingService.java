package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.services.team.TeamProvider;
import org.bukkit.entity.Player;

import java.util.*;

public interface MatchmakingService {

    /**
     * Add a player to the matchmaking queue.
     *
     * @param playerId the unique identifier of the player
     */
    void addPlayerToQueue(UUID playerId);

    /**
     * Add a player to the matchmaking queue.
     * @param player the player to add to the queue
     */
    void addPlayerToQueue(Player player);

    /**
     * Remove a player from the matchmaking queue.
     *
     * @param playerId the unique identifier of the player
     */
    void removePlayerFromQueue(UUID playerId);

    /**
     * Process the matchmaking queue and form teams.
     * Team forming will fail if the queue is not large enough to form teams.
     *
     * @return an optional list of player IDs grouped into teams
     */
    void startGameWithTeams(TeamProvider teamProvider);

    /**
     * Clear the matchmaking queue.
     */
    void clearQueue();

    /**
     * Get the current matchmaking queue.
     *
     * @return a list of player IDs in the queue
     */
    Queue<UUID> getQueue();
}
