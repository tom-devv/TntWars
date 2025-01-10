package dev.tom.tntWars.config.map;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.config.ConfigurationLoader;
import dev.tom.tntWars.models.map.TeamSpawnLocations;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A utility class for loading and saving map configurations from YAML files.
 * <p>
 * It's important to note that the names of individual yaml files, held inside
 * the MAPS_DIR directory do NOT correlate to the map names. The map name is stored
 * inside the config.
 */
public class MapConfigLoader implements ConfigurationLoader<MapConfig> {

    private final Path MAPS_DIR;
    private final String EXAMPLE_FILE_NAME = "example_config.yml";

    private static HashMap<String, MapConfig> mapConfigs = new HashMap<>();

    public MapConfigLoader(TNTWars plugin) {
        MAPS_DIR = plugin.getDataFolder().toPath().resolve("map_configs");
        if (Files.notExists(MAPS_DIR)) {
            try {
                Files.createDirectories(MAPS_DIR);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create map configs directory: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        try {
            createExampleFile();
        } catch (ConfigurateException e) {
            plugin.getLogger().warning("Failed to create example file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Load all map configurations from the map_configs directory.
     * This is particularly useful for loading all map configurations on plugin startup
     */
    @Override
    public void loadAll() throws ConfigurateException {
        HashMap<String, MapConfig> configs = new HashMap<>();
        File mapsDir = MAPS_DIR.toFile();
        if (!mapsDir.isDirectory()) return; // never
        for (File file : mapsDir.listFiles((f, name) -> name.endsWith(".yml"))) {
            if (file.getName().equalsIgnoreCase(EXAMPLE_FILE_NAME)) continue; // skip example file
            MapConfig mapConfig = loadConfig(file);
            configs.put(mapConfig.getMapName(), mapConfig);
        }
        mapConfigs = configs;
    }

    /**
     * Deserialize a single map configuration from a yaml yaml
     * @param file The file to load the map configuration from
     * @return A Pair of the map's name and the MapSpawnLocationsConfig object
     */
    @Override
    public MapConfig loadConfig(File file) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file.toPath()).build();
        ConfigurationNode rootNode = loader.load();
        MapConfig mapConfig = rootNode.get(MapConfig.class);

        if (mapConfig == null || mapConfig.getMapName() == null || mapConfig.getTeamSpawnLocations() == null) {
            throw new IllegalStateException("Invalid map configuration in file: " + file.getName());
        }

        // Use map-name as the key
        return mapConfig;
    }

    /**
     * Creates a default example YAML configuration file if it doesn't already exist.
     */
    private void createExampleFile() throws ConfigurateException {
        Path exampleFilePath = MAPS_DIR.resolve(EXAMPLE_FILE_NAME);

        try {
            Files.createDirectories(exampleFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Files.exists(exampleFilePath)) return;

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(exampleFilePath).build();
        ConfigurationNode rootNode = loader.load();

        MapConfig exampleConfig = MapConfig.defaultMapConfig();

        rootNode.set(MapConfig.class, exampleConfig);
        loader.save(rootNode);
    }

    /**
     * Fetches the MapSpawnLocationsConfig object for the given map name.
     * @param mapName
     * @return The MapSpawnLocationsConfig object for the given map name, or null if it doesn't exist.
     */
    public @Nullable MapConfig getConfig(String mapName) {
        return mapConfigs.get(mapName);
    }

    public static List<TeamSpawnLocations> cloneSpawnLocations(List<TeamSpawnLocations> originalList) {
        List<TeamSpawnLocations> clonedList = new ArrayList<>();
        for (TeamSpawnLocations teamSpawnLocation : originalList) {
            clonedList.add(teamSpawnLocation.clone());
        }
        return clonedList;
    }
}
