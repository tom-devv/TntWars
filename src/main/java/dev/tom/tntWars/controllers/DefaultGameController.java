package dev.tom.tntWars.controllers;

import com.mojang.brigadier.Message;
import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.models.game.GameStats;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.map.SpawnLocation;
import dev.tom.tntWars.models.map.TeamSpawnLocations;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class DefaultGameController extends Controller<UUID, Game> implements GameController {

    public DefaultGameController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public Optional<Game> findGame(GameSettings settings) {
        return Optional.empty();
    }

    @Override
    public Game createGame(Collection<Team> teams, GameSettings settings) {
        return new Game(settings, teams);
    }

    @Override
    public void startGame(Game game, CompletableFuture<Void> future) {
        GameStartEvent startEvent = new GameStartEvent(game);
        Bukkit.getServer().getPluginManager().callEvent(startEvent);
        if(startEvent.isCancelled()) return;

        TntWarsPlugin.getMapController().assignMap(game).thenAcceptAsync(map -> {
            System.out.println("Cloning map!");
            future.thenRun(() -> {
                System.out.println("Future complete, now moving");
                if(!enoughSpawns(game)) {
                    throw new RuntimeException("Not enough spawns to start game: " + game.getGameId());
                } else {
                    game.setState(GameState.ACTIVE);
                    // ensure that each participant is added to the instance map
                    // TODO add robust logging for if a player is already contained in the map
                    game.getParticipants().forEach(uuid -> {
                        instances.put(uuid, game);
                    });
                    // complete this sync
                    Bukkit.getScheduler().runTask(TntWarsPlugin.getPlugin(), () -> {
                        moveTeamsToGame(game);

                    });
                }
            });
        }).exceptionally(throwable -> {
            TntWarsPlugin.getPlugin().getLogger().severe("Error starting game (" + game.getGameId() + ") :" + throwable.getMessage());
            return null;
        });
    }

    @Override
    public void endGame(Game game) {
        GameEndEvent endEvent = new GameEndEvent(game);
        Bukkit.getServer().getPluginManager().callEvent(endEvent);
        if(endEvent.isCancelled()) return;

        game.setState(GameState.ENDED);
        game.getPlayers().forEach(player -> {
            MessageUtil.sendTitle(player,
                    "<green><bold> Game Over!</green>",
                    "<gray>Winners: <green>Team: " + game.getStats().getWinningTeam().getNumber() + " </green></gray>");
        });
        game.getParticipants().forEach(instances::remove); // remove uuid's from instance map
        movePlayersToLobby(game);
        TntWarsPlugin.getMapController().releaseMap(game);
    }

    @Override
    public void pauseGame(Game game) {
        game.setState(GameState.PAUSED);
    }

    @Override
    public void resumeGame(Game game) {
        game.setState(GameState.ACTIVE);
    }

    @Override
    public Game getGameById(String gameId) {
        return null;
    }


    @Override
    public Optional<Game> getSharedGame(Player player1, Player player2) {
        Optional<Game> game1 = getGameByPlayer(player1);
        Optional<Game> game2 = getGameByPlayer(player2);

        if (game1.isPresent() && game2.isPresent() && game1.get() == game2.get()) {
            return game1; // or game2, they refer to the same object
        }
        return Optional.empty();
    }

    @Override
    public void respawnPlayer(Game game, Player player) {
        game.getTeam(player).ifPresent(team -> {
            TeamSpawnLocations spawns = game.getMap().getSpawns().get(team.getNumber());
            spawns.getUnoccupied().spawnPlayer(player, game);
        });
    }

    @Override
    public Optional<Game> getGameByPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        Game game = getInstances().get(uuid);
        if(game == null) return Optional.empty();
        return Optional.of(game);
    }

    @Override
    public boolean isInGame(Player... players) {
        for (Player player : players) {
            UUID uuid = player.getUniqueId();
            boolean found = instances.containsKey(uuid);
            if(!found) return false;
        }
        return true;
    }

    @Override
    public void removePlayer(Game game, Player player) {
        game.getTeam(player).ifPresent(team -> {
            team.removePlayer(player.getUniqueId());
            instances.remove(player.getUniqueId());
            game.getParticipants().remove(player.getUniqueId());
            player.teleport(TntWarsPlugin.getLobbyLocation());
        });

    }

    private void movePlayersToLobby(Game game){
        game.getPlayers().forEach(player -> {
            player.teleport(TntWarsPlugin.getLobbyLocation());
        });
    }

    /**
     * A vital check to ensure that there are enough spawns for all the players
     * @param game
     * @return if there are enough spawns for the game's players
     */
    private boolean enoughSpawns(Game game){
        int participants = game.getParticipants().size();
        int spawns = game.getMap().getMaxPlayers();
        return participants < spawns;
    }

    /**
     * Moves teams to the game by "respawning" them
     * this simply teleports the player to an unoccupied spot
     * we also send a nice title with the game id
     * @param game
     */
    private void moveTeamsToGame(Game game){
        Map map = game.getMap();
        String title = "<red><bold>TNT Wars has started</bold> </red>";
        String subtitle = "<gray>Map: <red>" + map.getName() + "</red> | ID: <red>" + game.getGameId() +"</red></gray>";
        game.getPlayers().forEach(player -> {
            respawnPlayer(game, player);
            MessageUtil.sendTitle(player, title, subtitle);
        });

    }
}
