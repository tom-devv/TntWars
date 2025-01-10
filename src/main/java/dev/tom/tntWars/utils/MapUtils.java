package dev.tom.tntWars.utils;

import dev.tom.tntWars.config.map.MapConfig;
import dev.tom.tntWars.models.map.Map;
import org.bukkit.Location;


public class MapUtils {

    /**
     * Get the team region that the Location is within
     * @param map
     * @param location
     * @return the team number or -1 if the Location is not inside a team region
     */
    public static int getTeamRegion(Map map, Location location) {
        for (MapConfig.TeamRegion teamRegion : map.getConfig().getRegions().getTeamRegions()) {
            MapConfig.Point p1 = teamRegion.getP1();
            MapConfig.Point p2 = teamRegion.getP2();
            if (withinRegion(p1, p2, location)) {
                return teamRegion.getTeamNumber();
            }
        }
        return -1;
    }

    public static boolean withinCentreDivide(Map map, Location location){
        MapConfig config = map.getConfig();
        MapConfig.Point centre1 = config.getRegions().getCentre1();
        MapConfig.Point centre2 = config.getRegions().getCentre2();
        return withinRegion(centre1, centre2, location);
    }

    public static boolean withinExtent(Map map, Location location) {
        MapConfig config = map.getConfig();
        MapConfig.Point extent1 = config.getRegions().getExtent1();
        MapConfig.Point extent2 = config.getRegions().getExtent2();
        return withinRegion(extent1, extent2, location);
    }

    public static boolean withinRegion(MapConfig.Point p1, MapConfig.Point p2, Location location) {
        int minX = Math.min(p1.getX(), p2.getX());
        int maxX = Math.max(p1.getX(), p2.getX());

        int minY = Math.min(p1.getY(), p2.getY());
        int maxY = Math.max(p1.getY(), p2.getY());

        int minZ = Math.min(p1.getZ(), p2.getZ());
        int maxZ = Math.max(p1.getZ(), p2.getZ());

        int locX = location.getBlockX();
        int locY = location.getBlockY();
        int locZ = location.getBlockZ();

        return (locX >= minX && locX <= maxX) &&
                (locY >= minY && locY <= maxY) &&
                (locZ >= minZ && locZ <= maxZ);
    }

}
