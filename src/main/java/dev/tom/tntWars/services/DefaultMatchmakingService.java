package dev.tom.tntWars.services;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MatchmakingService;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.services.team.TeamProvider;
import org.bukkit.entity.Player;

import java.util.*;

public class DefaultMatchmakingService implements MatchmakingService {

//    private final TeamProvider teamProvider;
    private final GameSettings gameSettings;

    private final Queue<UUID> queue = new LinkedList<>();

    public DefaultMatchmakingService(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

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

    @Override
    public void addPlayerToQueue(Player player) {
        addPlayerToQueue(player.getUniqueId());
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
        if(queue.size() < teamProvider.minimumPlayersRequired()){
            return;
        }
        Collection<Team> teams = teamProvider.populateTeams(getQueue());
        clearQueue();
        Game game = TntWarsPlugin.getPlugin().getGameController().createGame(teams, gameSettings); // createGame requires game Settings
        TntWarsPlugin.getPlugin().getGameController().startGame(game);
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

    public GameSettings getGameSettings() {
        return gameSettings;
    }

}

