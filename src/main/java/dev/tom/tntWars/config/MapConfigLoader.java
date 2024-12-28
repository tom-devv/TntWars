package dev.tom.tntWars.config;

import dev.tom.tntWars.TntWarsPlugin;
import it.unimi.dsi.fastutil.Pair;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * A utility class for loading and saving map configurations from YAML files.
 * <p>
 * It's important to note that the names of individual yaml files, held inside
 * the MAPS_DIR directory do NOT correlate to the map names. The map name is stored
 * inside the config.
 */
public class MapConfigLoader {

    private final Path MAPS_DIR;
    private final String EXAMPLE_FILE_NAME = "example_spawnlocations.yml";

    private static HashMap<String, MapSpawnsConfig> mapConfigs = new HashMap<>();

    public MapConfigLoader(TntWarsPlugin plugin) {
        MAPS_DIR = plugin.getDataFolder().toPath().resolve("map_configs");
        if (Files.notExists(MAPS_DIR)) {
            try {
                Files.createDirectories(MAPS_DIR);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create map configs directory: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        createExampleFile();
    }

    /**
     * Load all map configurations from the map_configs directory.
     * This is particularly useful for loading all map configurations on plugin startup
     */
    public void loadAll() throws ConfigurateException {
        HashMap<String, MapSpawnsConfig> configs = new HashMap<>();
        File mapsDir = MAPS_DIR.toFile();
        if (!mapsDir.isDirectory()) return; // never
        for (File file : mapsDir.listFiles((_, name) -> name.endsWith(".yml"))) {
            if (file.getName().equalsIgnoreCase(EXAMPLE_FILE_NAME)) continue; // skip example file
            Pair<String, MapSpawnsConfig> map = loadMapConfig(file);
            configs.put(map.left(), map.right());
        }
        mapConfigs = configs;
    }

    /**
     * Deserialize a single map configuration from a yaml yaml
     * @param file The file to load the map configuration from
     * @return A Pair of the map's name and the MapSpawnLocationsConfig object
     */
    private Pair<String, MapSpawnsConfig> loadMapConfig(File file) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file.toPath()).build();
        ConfigurationNode rootNode = loader.load();
        MapSpawnsConfig mapConfig = rootNode.get(MapSpawnsConfig.class);

        if (mapConfig == null || mapConfig.getMapName() == null || mapConfig.getTeamSpawnLocations() == null) {
            throw new IllegalStateException("Invalid map configuration in file: " + file.getName());
        }

        // Use map-name as the key
        String mapName = mapConfig.getMapName();
        return Pair.of(mapName, mapConfig);
    }

    /**
     * Creates a default example YAML configuration file if it doesn't already exist.
     */
    private void createExampleFile() {
        Path exampleFilePath = MAPS_DIR.resolve(EXAMPLE_FILE_NAME);

        // Ensure the maps directory exists
        try {
            Files.createDirectories(exampleFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Files.exists(exampleFilePath)) return;


        // default example content
        String exampleContent = """
        # Example map configuration
        map-name: example_map # Unique map name for this template
        max-teams: 2
        team-spawn-locations:
          - team-number: 1
            locations:
              - x: 100
                y: 64
                z: 200
              - x: 150
                y: 65
                z: 250
          - team-number: 2
            locations:
              - x: 120
                y: 70
                z: 210
              - x: 180
                y: 75
                z: 260
        """;

        try {
            Files.writeString(exampleFilePath, exampleContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Created example map configuration file at: " + exampleFilePath);
    }

    /**
     * Serializes the MapSpawnLocationsConfig object back into YAML.
     */
    public void save(MapSpawnsConfig config) throws ConfigurateException, IOException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(MAPS_DIR.resolve(config.getMapName() + ".yml"))
                .build();

        ConfigurationNode rootNode = loader.createNode();
        rootNode.set(MapSpawnsConfig.class, config);
        loader.save(rootNode);
    }

    /**
     * Fetches the MapSpawnLocationsConfig object for the given map name.
     * @param mapName
     * @return The MapSpawnLocationsConfig object for the given map name, or null if it doesn't exist.
     */
    public static @Nullable MapSpawnsConfig getConfig(String mapName) {
        return mapConfigs.get(mapName);
    }
}
