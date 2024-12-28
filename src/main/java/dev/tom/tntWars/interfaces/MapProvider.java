package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.map.Map;

import java.util.concurrent.CompletableFuture;

/**
 * The MapProvider is responsible for providing the map.
 * The main job is to create the Map object and return it.
 */
public interface MapProvider {

    CompletableFuture<Map> getMap();


}
