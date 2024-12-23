package dev.tom.tntWars.services;

import dev.tom.tntWars.interfaces.MatchmakingService;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.services.team.TeamProvider;

import java.util.*;

public class DefaultMatchmakingService implements MatchmakingService {

    private final Queue<UUID> queue = new LinkedList<>();

    /**
     * Add a player to the matchmaking queue.
     *
     * @param playerId the unique identifier of the player
     */
    @Override
    public void addPlayerToQueue(UUID playerId) {
        if(queue.contains(playerId)) {
            return;
        }
        queue.add(playerId);
    }

    /**
     * Remove a player from the matchmaking queue.
     *
     * @param playerId the unique identifier of the player
     */
    @Override
    public void removePlayerFromQueue(UUID playerId) {
        queue.remove(playerId);
    }

    /**
     * Process the matchmaking queue and form teams.
     *
     * @param teamProvider
     * @return a list of player IDs grouped into teams
     */
    @Override
    public void startGameWithTeams(TeamProvider teamProvider) {
        //TODO: notify gamecontroller that a game is ready to start
//        if(queue.size() >= teamProvider.minimumPlayersRequired()) {
//            clearQueue();
//            return Optional.of(teamProvider.getTeams());
//        } else {
//            return Optional.empty();
//        }
    }

    /**
     * Clear the matchmaking queue.
     */
    @Override
    public void clearQueue() {
        queue.clear();
    }

    /**
     * Get the current matchmaking queue.
     *
     * @return a list of player IDs in the queue
     */
    @Override
    public Queue<UUID> getQueue() {
        return this.queue;
    }
}
