package dev.tom.tntWars.models.map;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * A list of spawn locations for a team.
 */
@ConfigSerializable
public class TeamSpawnLocations {

    private List<SpawnLocation> locations = new ArrayList<>();
    private int teamNumber;

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
        SpawnLocation loc = locations.get((int) (Math.random() * locations.size()));
        if (locations.isEmpty()) {
            return null;
        }
        return locations.get((int) (Math.random() * locations.size()));
    }

}
