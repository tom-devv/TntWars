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
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.map.SpawnLocation;
import dev.tom.tntWars.models.map.TeamSpawnLocations;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;

public class DefaultGameController extends Controller implements GameController {

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
    public void startGame(Game game) {
        GameStartEvent startEvent = new GameStartEvent(game);
        Bukkit.getServer().getPluginManager().callEvent(startEvent);
        if(startEvent.isCancelled()) return;

        TntWarsPlugin.getMapController().assignMap(game).thenAcceptAsync(map -> {
            Bukkit.getScheduler().runTask(TntWarsPlugin.getPlugin(), () -> {
                enoughSpawns(game);
                moveTeamsToGame(game);
                game.setState(GameState.ACTIVE);
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
        TntWarsPlugin.getMapController().releaseMap(game);
    }

    @Override
    public void pauseGame(Game game) {
        game.setState(GameState.PAUSED);
    }

    @Override
    public void resumeGame(Game game) {

    }

    @Override
    public Game getGameById(String gameId) {
        return null;
    }

    private void movePlayersToLobby(Game game){
        game.getParticipants()
                .stream().map(Bukkit::getPlayer).filter(Objects::nonNull)
                .forEach(player -> {
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
     * Moves teams to the game by spawning them in their respective slots
     * @param game
     */
    private void moveTeamsToGame(Game game){
        List<Team> teams = new ArrayList<>(game.getTeams());
        Map map = game.getMap();
        World world = map.getWorld();

        String title = "<red><bold>TNT Wars has started</bold> </red>";
        String subtitle = "<gray>Map: <red>" + map.getName() + "</red> | ID: <red>" + game.getGameId() +"</red></gray>";

        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            TeamSpawnLocations spawns = map.getTeamSpawnLocations().get(i);
            for (UUID playerUUID : team.getPlayerUUIDs()) {
                Player player = Bukkit.getPlayer(playerUUID);
                if (player != null) {
                    spawns.getUnoccupied().spawnPlayer(player, world);
                    MessageUtil.sendTitle(player, title, subtitle);
                }
            }
        }
    }
}
