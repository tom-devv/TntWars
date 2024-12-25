package dev.tom.tntWars.models.map;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public class SpawnLocation {

    private final Location location;
    private boolean occupied = false;

    public SpawnLocation(Location location){
        this.location = location;
    }

    public void spawnPlayer(Player player){
        player.teleport(location);
        occupied = true;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Location getLocation() {
        return location;
    }
}
