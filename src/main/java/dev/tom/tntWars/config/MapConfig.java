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

    // Store regions as a nested object with extent and centre
    @Comment("Map regions including extent and center points")
    private Region regions;

    public String getMapName() {
        return mapName;
    }

    public Region getRegions() {
        return regions;
    }

    public void setRegions(Region regions) {
        this.regions = regions;
    }

    /**
     * @deprecated do not use this, it is for serialization only
     * @return Returns the team spawn location
     */
    public List<TeamSpawnLocations> getTeamSpawnLocations() {
        return Collections.unmodifiableList(teamSpawnLocations);
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

    public static @NotNull MapConfig defaultMapConfig() {
        MapConfig exampleConfig = new MapConfig();
        exampleConfig.setMapName("example_map");
        exampleConfig.setMaxTeams(2);

        Region region = new Region(
                new Point(-57, -64, -2),
                new Point(67, 321, 54),
                new Point(0, 0, 0),
                new Point(0, 100, 100),
                List.of(new TeamRegion(1, new Point(0,100,10), new Point(0,-64,100)))
        );
        exampleConfig.setRegions(region);

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

    @ConfigSerializable
    public static class Region {
        @Comment("The two points defining the extent (boundary) of the map")
        private Point extent1, extent2;

        @Comment("The two points defining the center area of the map")
        private Point centre1, centre2;

        private List<TeamRegion> teamRegions;

        public Region(Point extent1, Point extent2, Point centre1, Point centre2, List<TeamRegion> teamRegions) {
            this.extent1 = extent1;
            this.extent2 = extent2;
            this.centre1 = centre1;
            this.centre2 = centre2;
            this.teamRegions = teamRegions;
        }

        public Region() {

        }

        public List<TeamRegion> getTeamRegions() {
            return this.teamRegions;
        }

        public void setTeamRegions(List<TeamRegion> teamRegions) {
            this.teamRegions = teamRegions;
        }

        public Point getExtent1() {
            return extent1;
        }

        public Point getExtent2() {
            return extent2;
        }

        public Point getCentre1() {
            return centre1;
        }

        public Point getCentre2() {
            return centre2;
        }

        public void setExtent2(Point extent2) {
            this.extent2 = extent2;
        }

        public void setExtent1(Point extent1) {
            this.extent1 = extent1;
        }

        public void setCentre1(Point centre1) {
            this.centre1 = centre1;
        }

        public void setCentre2(Point centre2) {
            this.centre2 = centre2;
        }
    }

    @ConfigSerializable
    public static class TeamRegion {
        private int teamNumber;
        private Point p1, p2;

        public TeamRegion(int teamNumber, Point p1, Point p2) {
            this.teamNumber = teamNumber;
            this.p1 = p1;
            this.p2 = p2;
        }

        public TeamRegion() {}

        public Point getP1() {
            return p1;
        }

        public void setP1(Point p1) {
            this.p1 = p1;
        }

        public int getTeamNumber() {
            return teamNumber;
        }

        public void setTeamNumber(int teamNumber) {
            this.teamNumber = teamNumber;
        }

        public Point getP2() {
            return p2;
        }

        public void setP2(Point p2) {
            this.p2 = p2;
        }
    }

    @ConfigSerializable
    public static class Point {
        private int x, y, z;

        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Point() {}

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
