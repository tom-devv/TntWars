package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.game.GameState;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class DefaultMapController extends Controller implements MapController {

    private final java.util.Map<Game, Map> activeGameMaps = new HashMap<>();

    public DefaultMapController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public Map assignMap(Game game) {
        // A map is already assigned to this game, cannot assign a new one
        if(activeGameMaps.containsKey(game)){
            return activeGameMaps.get(game);
        }
        MapProvider provider = game.getSettings().getMapProvider();
        Map map = provider.getMap();
        activeGameMaps.put(game, map);
        game.setMap(map);
        return null;
    }

    @Override
    public void releaseMap(Game game, Map map) {
        // The game is not finished, cannot release the map
        if (game.getState() != GameState.ENDED) {
            throw new IllegalStateException("Cannot release map while the game is not finished.");
        }
        for (Player player : map.getWorld().getPlayers()) {
            // player.teleport() TODO: get them out of the world
        }
        activeGameMaps.remove(game);
        game.setMap(null);
        deleteMap(map);
    }

    @Override
    public void deleteMap(Map map) {

    }

    @Override
    public int getMapsInUse() {
        return 0;
    }
}
