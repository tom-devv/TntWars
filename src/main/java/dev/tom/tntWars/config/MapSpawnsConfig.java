package dev.tom.tntWars.config;

import dev.tom.tntWars.models.map.TeamSpawnLocations;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MapSpawnsConfig {

    private String mapName;
    private int maxTeams;
    private List<TeamSpawnLocations> teamSpawnLocations;

    public String getMapName() {
        return mapName;
    }

    /**
     * Gets the team spawn locations
     * @return a COPY of the spawn locations
     */
    public List<TeamSpawnLocations> getTeamSpawnLocations() {
        return teamSpawnLocations;
    }

    public void setTeamSpawnLocations(List<TeamSpawnLocations> teamSpawnLocations) {
        this.teamSpawnLocations = teamSpawnLocations;
    }

    public int getMaxTeams() {
        return maxTeams;
    }

    public void setMaxTeams(int maxTeams) {
        this.maxTeams = maxTeams;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}

