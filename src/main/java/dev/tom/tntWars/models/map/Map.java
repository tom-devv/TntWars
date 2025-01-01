package dev.tom.tntWars.models.map;


import dev.tom.tntWars.config.MapConfigLoader;
import dev.tom.tntWars.config.MapConfig;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Maps are instanced per-game and have a world associated with them.
 * This world is the cloned world and is the one used for the specific game.
 */
public class Map {

    /**
     * The world associated with this map.
     */
    private World world;
    /**
     * The name of the map.
     * Useful for getting information about this map.
     */
    private final String name;
    private final java.util.Map<Integer, TeamSpawnLocations> spawns;
    /**
     * This List is just for convenience to get all locations rather than per team
     */
    private final List<SpawnLocation> spawnLocations = new ArrayList<>();
    /**
     * This is equivalent to the number of spawn locations available
     */
    private final int maxPlayers;
    /**
     * The config associated with the map
     */
    private final MapConfig mapConfig;

    public Map(String name, World world) {
        this.world = world;
        this.name = name;
        this.spawns = fetchSpawnLocations();
        this.maxPlayers = fetchMaxPlayers();
        this.mapConfig = MapConfigLoader.getConfig(name);
        if(this.mapConfig == null) throw new RuntimeException("Failed to fetch config for map: " + name + " is map_name correct?");
    }

    private int fetchMaxPlayers(){
        int total = 0;
        for (TeamSpawnLocations spawn : this.spawns.values()) {
            total +=spawn.getLocations().size();
        }
        return total;
    }

    /**
     * Fetch the spawn locations from the preloaded config files and return them.
     * @return a CLONE of the spawn locations as the instance of them is unique to this map instance
     */
    private java.util.Map<Integer, TeamSpawnLocations> fetchSpawnLocations(){
        java.util.Map<Integer, TeamSpawnLocations> spawnMap = new HashMap<>();
        MapConfig config = MapConfigLoader.getConfig(this.name);
        if(config == null) throw new RuntimeException("Failed to fetch config for map: " + this.name);
        MapConfigLoader.cloneSpawnLocations(config.getTeamSpawnLocations()).forEach(spawn -> {
            spawnMap.put(spawn.getTeamNumber(), spawn);
        });
        return spawnMap;
    }

    /**
     * Reset spawn locations so that they are all usable again
     */
    public void setSpawnsUnoccupied(){
        spawnLocations.forEach(spawn -> {
            spawn.setOccupied(false);
        });
    }

    public void unloadWorld(){
        this.world = null;
    }

    public java.util.Map<Integer, TeamSpawnLocations> getSpawns() {
        return spawns;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public MapConfig getConfig() {
        return mapConfig;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

}
