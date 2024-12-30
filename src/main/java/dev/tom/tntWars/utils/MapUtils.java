package dev.tom.tntWars.utils;

import dev.tom.tntWars.config.MapConfig;
import dev.tom.tntWars.models.map.Map;
import org.bukkit.Location;


public class MapUtils {

    public static boolean withinCentreDivide(Map map, Location location){
        MapConfig config = map.getConfig();
        MapConfig.Point centre1 = config.getCentrePoint1();
        MapConfig.Point centre2 = config.getCentrePoint2();

        int minX = Math.min(centre1.getX(), centre2.getX());
        int maxX = Math.max(centre1.getX(), centre2.getX());

        int minY = Math.min(centre1.getY(), centre2.getY());
        int maxY = Math.max(centre1.getY(), centre2.getY());

        int minZ = Math.min(centre1.getZ(), centre2.getZ());
        int maxZ = Math.max(centre1.getZ(), centre2.getZ());

        int locX = location.getBlockX();
        int locY = location.getBlockY();
        int locZ = location.getBlockZ();

        return (locX >= minX && locX <= maxX) &&
                (locY >= minY && locY <= maxY) &&
                (locZ >= minZ && locZ <= maxZ);
    }

    public static boolean withinExtent(Map map, Location location) {
        MapConfig config = map.getConfig();
        MapConfig.Point extent1 = config.getMapExtent1();
        MapConfig.Point extent2 = config.getMapExtent2();

        int minX = Math.min(extent1.getX(), extent2.getX());
        int maxX = Math.max(extent1.getX(), extent2.getX());

        int minY = Math.min(extent1.getY(), extent2.getY());
        int maxY = Math.max(extent1.getY(), extent2.getY());

        int minZ = Math.min(extent1.getZ(), extent2.getZ());
        int maxZ = Math.max(extent1.getZ(), extent2.getZ());

        int locX = location.getBlockX();
        int locY = location.getBlockY();
        int locZ = location.getBlockZ();

        return (locX >= minX && locX <= maxX) &&
                (locY >= minY && locY <= maxY) &&
                (locZ >= minZ && locZ <= maxZ);
    }

}
