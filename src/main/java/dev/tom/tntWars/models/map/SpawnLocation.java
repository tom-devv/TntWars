package dev.tom.tntWars.models.map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class SpawnLocation implements Cloneable{

    private double x,y,z;
    private int teamNumber;
    private transient boolean occupied = false;

    public void setZ(double z) {
        this.z = z;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void spawnPlayer(Player player, World world){
        player.teleport(new Location(world, x, y, z));
        occupied = true;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public int getTeamNumber() {
        return teamNumber;
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
