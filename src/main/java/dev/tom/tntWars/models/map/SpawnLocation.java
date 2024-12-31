package dev.tom.tntWars.models.map;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpawnLocation implements Cloneable {

    private double x,y,z;
    private transient boolean occupied = false;

    public SpawnLocation(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SpawnLocation() {}

    public void setZ(double z) {
        this.z = z;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }


    public void spawnPlayer(Player player, Game game){
        player.teleport(new Location(game.getMap().getWorld(), x, y, z));
        occupied = true;
        freeSpawnWithDelay(game.getSettings().getRespawnDelaySeconds());
    }

    /**
     * After a spawn has been used it should be made 'available' after a certain time period
     * @param delaySeconds
     */
    private void freeSpawnWithDelay(int delaySeconds){
        Bukkit.getScheduler().runTaskLater(TntWarsPlugin.getPlugin(), new Runnable() {
            @Override
            public void run() {
                setOccupied(false);
            }
        }, 20L * delaySeconds);
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }


    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    @Override
    public SpawnLocation clone() {
        try {
            SpawnLocation copy = (SpawnLocation) super.clone();
            // Example of deep-copying a mutable object
            // copy.someList = new ArrayList<>(this.someList);
            copy.occupied = false; // Reset transient field
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }
}
