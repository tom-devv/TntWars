package dev.tom.tntWars.models.map;

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

    /**
     * Get a random spawn location from the list of spawn locations.
     * @return
     */
    public SpawnLocation getRandomSpawnLocation() {
        if (locations.isEmpty()) {
            return null;
        }
        return locations.get((int) (Math.random() * locations.size()));
    }


}
