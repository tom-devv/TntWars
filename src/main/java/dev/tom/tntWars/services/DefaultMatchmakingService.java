package dev.tom.tntWars.services;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MatchmakingService;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.services.team.TeamProvider;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.CompletableFuture;

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
        MessageUtil.sendMini(playerId, "<green>You have joined the queue. Position: #" + getQueue().size());
        startGameWithTeams(TntWarsPlugin.getBalancedTeamProvider());
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
        MessageUtil.sendMini(playerId, "<green>You have left the queue.");
    }

    /**
     * Process the matchmaking queue and form teams.
     *
     * @param teamProvider this should be changed in the future but right now it's static inside the Plugin class
     * @return a list of player IDs grouped into teams
     */
    @Override
    public void startGameWithTeams(TeamProvider teamProvider) {
        if(queue.size() < teamProvider.minimumPlayersRequired()) return;
        Collection<Team> teams = teamProvider.populateTeams(getQueue());
        Game game = TntWarsPlugin.getGameController().createGame(teams, gameSettings);
        broadcastTimer(getQueue().stream().map(Bukkit::getPlayer).toList(), 5).thenRun(() -> {
            TntWarsPlugin.getGameController().startGame(game);
        }).exceptionally(throwable ->  {
            throwable.printStackTrace();
            return null;
        });
        // clear queue after passing to broadcastTimer as it requires the players to broadcast to
        clearQueue();
    }

    /**
     * Broadcasts a timer to a collection of players
     * @param players to broadcast to, this is normally the queue
     * @param seconds
     * @return a future
     */
    private CompletableFuture<Void> broadcastTimer(Collection<Player> players, int seconds) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        // Create a Bukkit task that will handle the countdown
        new BukkitRunnable() {
            int remainingTime = seconds;

            @Override
            public void run() {
                if (remainingTime <= 0) {
                    // Timer has finished, complete the future and cancel the task
                    future.complete(null); // Completing the future when the timer finishes
                    this.cancel();
                } else {
                    // Optionally, log or display the remaining time
                    for (Player player : players) {
                        MessageUtil.sendTitle(player, "<red> Game starting </red>", "<gray> In " + remainingTime + " seconds </gray>");
                    }
                    remainingTime--; // Decrease the time
                }
            }
        }.runTaskTimer(TntWarsPlugin.getPlugin(), 0 ,20);
        return future;
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

