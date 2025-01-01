package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class DefaultMapController extends Controller<Game, Map> implements MapController {

    public DefaultMapController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public CompletableFuture<Map> assignMap(Game game) {
        // Check if a map is already assigned to this game
        if (instances.containsKey(game)) {
            return CompletableFuture.completedFuture(instances.get(game));
        }

        MapProvider provider = game.getSettings().getMapProvider();

        // Retrieve the map asynchronously and handle assignment
        return provider.getMap().thenApply(map -> {
            instances.put(game, map);
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
        System.out.println("Teleported all players out of world");
        World worldToDelete = map.getWorld();
        instances.remove(game);
        map.unloadWorld();
        game.setMap(null);
        TntWarsPlugin.getWorldManager().deleteWorld(worldToDelete);
    }


    @Override
    public int getMapsInUse() {
        return 0;
    }
}
