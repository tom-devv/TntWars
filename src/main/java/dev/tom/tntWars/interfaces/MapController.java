package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.game.Game;

import dev.tom.tntWars.models.map.Map;

import java.util.concurrent.CompletableFuture;

/**
 * The MapController is responsible for managing the maps available for games.
 * It handles the Map lifecycle
 */
public interface MapController {

    /**
     * Assign a map to a game
     * @param game
     * @return the map assigned to the game
     */
    CompletableFuture<Map> assignMap(Game game);

    /**
     * Handles the cleanup of a map when a game ends
     * @param game The game that used the map.
     * @param map  The map to clean up.
     */
    void releaseMap(Game game);

    /**
     * Deletes the actual Map world files
     * @param map
     */
    void deleteMap(Map map);

    /**
     * @return the number of maps being used by games right now
     */
    int getMapsInUse();

}
