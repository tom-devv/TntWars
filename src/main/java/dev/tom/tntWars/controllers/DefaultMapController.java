package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DefaultMapController extends Controller implements MapController {

    private final java.util.Map<Game, Map> activeGameMaps = new HashMap<>();

    public DefaultMapController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<Map> assignMap(Game game) {
        // Check if a map is already assigned to this game
        if (activeGameMaps.containsKey(game)) {
            return CompletableFuture.completedFuture(activeGameMaps.get(game));
        }

        MapProvider provider = game.getSettings().getMapProvider();

        // Retrieve the map asynchronously and handle assignment
        return provider.getMap().thenApply(map -> {
            activeGameMaps.put(game, map);
            game.setMap(map);
            return map;
        });
    }


    @Override
    public void releaseMap(Game game) {
        Map map = game.getMap();
        if (map == null) {
            throw new IllegalStateException("No map assigned to this game.");
        }
        // The game is not finished, cannot release the map
        if (game.getState() != GameState.ENDED) {
            throw new IllegalStateException("Cannot release map while the game is not finished.");
        }
        // if for some reason all the players aren't already gone
        for (Player player : map.getWorld().getPlayers()) {
             player.teleport(TntWarsPlugin.getLobbyLocation());
        }
        activeGameMaps.remove(game);
        game.setMap(null);
        deleteMap(map);
    }

    @Override
    public void deleteMap(Map map) {
        World world = map.getWorld();
        File worldFile = world.getWorldFolder();
        Bukkit.unloadWorld(world, false);
        Bukkit.getScheduler().runTaskAsynchronously(TntWarsPlugin.getPlugin(), worldFile::delete);
    }

    @Override
    public int getMapsInUse() {
        return 0;
    }
}
