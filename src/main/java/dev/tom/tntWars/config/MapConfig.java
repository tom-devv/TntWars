package dev.tom.tntWars.config;

import dev.tom.tntWars.models.map.SpawnLocation;
import dev.tom.tntWars.models.map.TeamSpawnLocations;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Collections;
import java.util.List;

@ConfigSerializable
public class MapConfig {

    @Comment("This is the name of the map folder inside of map_templates")
    private String mapName;
    private int maxTeams;
    @Comment("""
            This is a list of spawn locations for each team, for example if a Map has
            two teams then you should define spawn locations for
            team-number: 1 and team-number: 2, ideally you should have 5 locations per
            team because most maps have a max of 10 players, although you can define more
            if you want to increase spawn randomisation for respawns mid game
            """)

    private List<TeamSpawnLocations> teamSpawnLocations;
    @Comment("Two locations which define the centre part, players will not be able to cross over this area")
    private Point centrePoint1, centrePoint2;
    @Comment("""
            These should be the two corners of the map, you should set the Y to -64 and 320
            the corners create the 'region' the players can play within
            """)
    private Point mapExtent1, mapExtent2;

    public String getMapName() {
        return mapName;
    }

    /**
     * @deprecated do not use this, it is for serialization only
     * @return Returns the team spawn location
     */
    public List<TeamSpawnLocations> getTeamSpawnLocations() {
        return Collections.unmodifiableList(teamSpawnLocations);
    }

    public Point getMapExtent2() {
        return mapExtent2;
    }

    public void setMapExtent2(Point mapExtent2) {
        this.mapExtent2 = mapExtent2;
    }

    public Point getMapExtent1() {
        return mapExtent1;
    }

    public void setMapExtent1(Point mapExtent1) {
        this.mapExtent1 = mapExtent1;
    }

    public Point getCentrePoint1() {
        return centrePoint1;
    }

    public Point getCentrePoint2() {
        return centrePoint2;
    }

    public void setCentrePoint1(Point centrePoint1) {
        this.centrePoint1 = centrePoint1;
    }

    public void setCentrePoint2(Point centrePoint2) {
        this.centrePoint2 = centrePoint2;
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

    /**
     * Creates a default MapConfig instance
     * @return a default instance for MapConfig
     */
    public static @NotNull MapConfig defaultMapConfig() {
        MapConfig exampleConfig = new MapConfig();
        exampleConfig.setMapName("example_map");
        exampleConfig.setMaxTeams(2);
        exampleConfig.setMapExtent1(new MapConfig.Point(-57,-64, -2));
        exampleConfig.setMapExtent2(new MapConfig.Point(67,321, 54));
        exampleConfig.setCentrePoint1(new MapConfig.Point(0,0,0));
        exampleConfig.setCentrePoint2(new MapConfig.Point(0,100,100));

        TeamSpawnLocations teamOne = new TeamSpawnLocations(1,
                List.of(
                        new SpawnLocation(-32,-59,3),
                        new SpawnLocation(-36,-59,29)
                ));
        TeamSpawnLocations teamTwo = new TeamSpawnLocations(2,
                List.of(
                        new SpawnLocation(56,-52,26),
                        new SpawnLocation(51,-59,16)
                ));

        exampleConfig.setTeamSpawnLocations(List.of(teamOne, teamTwo));
        return exampleConfig;
    }

    /**
     * Represents a world-independent location, this is because storing
     * a Bukkit Location in a config requires a world, worlds for maps
     * are not known until the map is created
     */
    @ConfigSerializable
    public static class Point {
        private int x;
        private int y;
        private int z;

        public Point(int x, int y, int z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point(){}

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setZ(int z) {
            this.z = z;
        }
    }
}



