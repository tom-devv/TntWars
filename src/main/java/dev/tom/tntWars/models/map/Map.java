package dev.tom.tntWars.models.map;


import dev.tom.tntWars.config.MapConfigLoader;
import dev.tom.tntWars.config.MapConfig;
import org.bukkit.World;

import java.util.List;

/**
 * Maps are instanced per-game and have a world associated with them.
 * This world is the cloned world and is the one used for the specific game.
 */
public class Map {

    /**
     * The world associated with this map.
     */
    private final World world;
    /**
     * The name of the map.
     * Useful for getting information about this map.
     */
    private final String name;
    private final List<TeamSpawnLocations> spawns;
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
        for (TeamSpawnLocations spawn : this.spawns) {
            total +=spawn.getLocations().size();
        }
        return total;
    }

    /**
     * Fetch the spawn locations from the preloaded config files and return them.
     * @return a CLONE of the spawn locations as the instance of them is unique to this map instance
     */
    private List<TeamSpawnLocations> fetchSpawnLocations(){
        MapConfig config = MapConfigLoader.getConfig(this.name);
        if(config == null) throw new RuntimeException("Failed to fetch config for map: " + this.name);
        return MapConfigLoader.cloneSpawnLocations(config.getTeamSpawnLocations());
    }

    public List<TeamSpawnLocations> getTeamSpawnLocations() { return this.spawns; }

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
