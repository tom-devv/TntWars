package dev.tom.tntWars.config;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.models.map.Map;
import it.unimi.dsi.fastutil.Pair;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class MapConfigLoader {

    private final Path MAPS_DIR;
    private final String EXAMPLE_FILE_NAME = "example_spawnlocations.yml";



    private MapConfig mapConfig;



    public MapConfigLoader() {
        MAPS_DIR = TntWarsPlugin.getPlugin().getDataFolder().toPath().resolve("map_configs");
        if(Files.notExists(MAPS_DIR)) {
            try {
                Files.createDirectories(MAPS_DIR);
            } catch (IOException e) {
                TntWarsPlugin.getPlugin().getLogger().severe("Failed to create map configs directory: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, MapConfig> loadAll(){
        HashMap<String, MapConfig> configs = new HashMap<>();
        File mapsDir = MAPS_DIR.toFile();
        if(!mapsDir.isDirectory()) return null; // never
        for (File file : mapsDir.listFiles((_, name) -> name.endsWith(".yml"))) {
            Pair<String, MapConfig> map = null;
            try {
                map = loadMapConfig(file);
            } catch (ConfigurateException e) {
                throw new RuntimeException(e);
            }
            configs.put(map.left(), map.right());
        }
        return configs;
    }

    public Pair<String, MapConfig> loadMapConfig(File file) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file.toPath()).build();

        ConfigurationNode rootNode = loader.load();
        MapConfig mapConfig = rootNode.get(MapConfig.class);

        if (mapConfig == null || mapConfig.getWorld() == null) {
            throw new IllegalStateException("Invalid map configuration in file: " + file.getName());
        }

        // Use map-name as the key
        String mapName = rootNode.node("world").getString();
        if (mapName == null || mapName.isEmpty()) {
            throw new IllegalStateException("Missing or empty map-name in file: " + file.getName());
        }

        return Pair.of(mapName, mapConfig);
    }


    /**
     * Creates a default example YAML configuration file if it doesn't already exist.
     *
     * @throws IOException if an error occurs while creating the file
     */
    public  void createExampleFile() {
        Path exampleFilePath = MAPS_DIR.resolve(EXAMPLE_FILE_NAME);

        // Ensure the maps directory exists
        try {
            Files.createDirectories(exampleFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Check if the file already exists
        if (Files.exists(exampleFilePath)) {
            System.out.println("Example file already exists: " + exampleFilePath);
            return;
        }

        // Write the default configuration content
        String exampleContent = """
            # Example map configuration
            map-name: example_map # Unique map name for this template
            world: example_world  # The world name for this map
            spawn-locations:
              - x: 100
                y: 64
                z: 200
                team-number: 1
              - x: 150
                y: 65
                z: 250
                team-number: 2
              - x: 120
                y: 70
                z: 210
                team-number: 2
        """;

        try {
            Files.writeString(exampleFilePath, exampleContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Created example map configuration file at: " + exampleFilePath);
    }

    public MapConfig getMapConfig() {
        return mapConfig;
    }
}
