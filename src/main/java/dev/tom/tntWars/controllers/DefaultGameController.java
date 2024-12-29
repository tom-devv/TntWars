package dev.tom.tntWars.controllers;

import com.mojang.brigadier.Message;
import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.models.map.Map;
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
        TntWarsPlugin.getMapController().assignMap(game).thenAcceptAsync(map -> {
            Bukkit.getScheduler().runTask(TntWarsPlugin.getPlugin(), () -> {
                moveTeamsToGame(game);
            });
        }).exceptionally(throwable -> {
            TntWarsPlugin.getPlugin().getLogger().severe("Error starting game (" + game.getGameId() + ") :" + throwable.getMessage());
            return null;
        });
    }

    private void moveTeamsToGame(Game game){
        List<Team> teams = new ArrayList<>(game.getTeams());
        Map map = game.getMap();
        World world = map.getWorld();
        String title = "<red><bold>TNT Wars has started</bold> </red>";
        String subtitle = "<gray>Map: <red>" + map.getName() + "</red> | Objective: <red>Raid</red></gray>";
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



    @Override
    public void endGame(Game game) {
        TntWarsPlugin.getMapController().releaseMap(game);
    }

    @Override
    public void pauseGame(Game game) {

    }

    @Override
    public void resumeGame(Game game) {

    }

    @Override
    public Game getGameById(String gameId) {
        return null;
    }
}
