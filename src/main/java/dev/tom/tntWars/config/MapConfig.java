package dev.tom.tntWars.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MapConfig {

    private String world;
    private List<SpawnLocationConfig> spawnLocations;

    public String getWorld() {
        return world;
    }

    public List<SpawnLocationConfig> getSpawnLocations() {
        return spawnLocations;
    }

    @ConfigSerializable
    public static class SpawnLocationConfig {
        private double x;
        private double y;
        private double z;
        private int teamNumber;

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public double getZ() {
            return z;
        }

        public int getTeamNumber() {
            return teamNumber;
        }
    }
}
