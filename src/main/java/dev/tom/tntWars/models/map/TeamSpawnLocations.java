package dev.tom.tntWars.models.map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of spawn locations for a team.
 */
@ConfigSerializable
public class TeamSpawnLocations implements Cloneable {

    private List<SpawnLocation> locations = new ArrayList<>();
    private int teamNumber;

    public TeamSpawnLocations(int teamNumber, List<SpawnLocation> locations) {
        this.locations = locations;
        this.teamNumber = teamNumber;
    }

    public TeamSpawnLocations() {}

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public void setLocations(List<SpawnLocation> locations) {
        this.locations = locations;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    /**
     *  Get the list of spawn locations.
     * @return
     */
    public List<SpawnLocation> getLocations() {
        return locations;
    }

    /**
     * Get the number of spawn locations in the list.
     * @return
     */
    public int getNumberOfLocations() {
        return locations.size();
    }

    public SpawnLocation getUnoccupied(){
        SpawnLocation loc = getRandom();
        if(loc == null) return null;
        int count = 0;
        while(loc.isOccupied()){
            if(count > 1000) {
                throw new RuntimeException("Failed to find unoccupied spawn location after 1000 attempts. Why?");
            }
            loc = getRandom();
            count++;
        }
        return loc;
    }

    /**
     * Get a random unoccupied spawn location from the list of spawn locations.
     * @return
     */
    private SpawnLocation getRandom() {
        if (locations.isEmpty()) {
            return null;
        }
        return locations.get((int) (Math.random() * locations.size()));
    }

    @Override
    public TeamSpawnLocations clone() {
        try {
            // Perform a shallow copy
            TeamSpawnLocations copy = (TeamSpawnLocations) super.clone();

            // Deep copy the locations list
            List<SpawnLocation> clonedLocations = new ArrayList<>();
            for (SpawnLocation location : this.locations) {
                clonedLocations.add(location.clone()); // Clone each SpawnLocation
            }
            copy.setLocations(clonedLocations);

            return copy;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e); // This should not happen as we implement Cloneable
        }
    }
}
