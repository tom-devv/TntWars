package dev.tom.tntWars.models.map;


import org.bukkit.World;

/**
 * Maps are instanced per-game and have a world associated with them.
 * This world is the cloned world and is the one used for the specific game.
 */
public class Map {

    private final World world;

    public Map(World world){
        this.world = world;
    }

    public World getWorld() {
        return world;
    }
}
