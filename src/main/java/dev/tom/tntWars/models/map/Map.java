package dev.tom.tntWars.models.map;


import dev.tom.tntWars.config.MapConfigLoader;
import dev.tom.tntWars.config.MapSpawnsConfig;
import org.bukkit.World;

import java.util.ArrayList;
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

    public Map(String name, World world) {
        this.world = world;
        this.name = name;
        this.spawns = fetchSpawnLocations();
    }

    /**
     * Fetch the spawn locations from the preloaded config files and return them.
     * @return a CLONE of the spawn locations as the instance of them is unique to this map instance
     */
    private List<TeamSpawnLocations> fetchSpawnLocations(){
        MapSpawnsConfig config = MapConfigLoader.getConfig(this.name);
        if(config == null) throw new RuntimeException("Failed to fetch config for map: " + this.name);
        return new ArrayList<>(config.getTeamSpawnLocations());
    }

    public List<TeamSpawnLocations> getTeamSpawnLocations() {
        return this.spawns;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }
}
