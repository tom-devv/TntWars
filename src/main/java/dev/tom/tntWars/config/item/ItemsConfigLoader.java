package dev.tom.tntWars.config.item;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.config.ConfigurationLoader;
import dev.tom.tntWars.config.map.MapConfig;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class ItemsConfigLoader implements ConfigurationLoader<ItemConfig> {

    private final Path ITEMS_DIR;
    private final String EXAMPLE_FILE_NAME = "example_items.yml";
    private static HashMap<String, ItemConfig> itemsConfigs = new HashMap<>();

    public ItemsConfigLoader(TNTWars plugin) {
        this.ITEMS_DIR = plugin.getDataFolder().toPath().resolve("items_configs");
        if (Files.notExists(ITEMS_DIR)) {
            try {
                Files.createDirectories(ITEMS_DIR);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create items directory: " + e.getMessage(), e);
            }
        }
        try {
            createExampleFile();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create example file: " + e.getMessage(), e);
        }
    }

    @Override
    public void loadAll() throws ConfigurateException {
        File itemsDir = ITEMS_DIR.toFile();
        if (!itemsDir.isDirectory()) return;
        for (File file : itemsDir.listFiles((f, name) -> name.endsWith(".yml"))) {
            ItemConfig ItemsConfig = loadConfig(file);
            itemsConfigs.put(ItemsConfig.getName(), ItemsConfig);
        }
    }

    @Override
    public ItemConfig loadConfig(File file) throws ConfigurateException {
        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(file.toPath()).build();
        ConfigurationNode rootNode = loader.load();
        ItemConfig itemsConfig = rootNode.get(ItemConfig.class);

        if (itemsConfig == null || itemsConfig.getName() == null) {
            throw new IllegalStateException("Invalid item configuration in file: " + file.getName());
        }

        // parse items now
        itemsConfig.parse();

        return itemsConfig;
    }

    /**
     * Creates a default example YAML configuration file if it doesn't already exist.
     */
    private void createExampleFile() throws ConfigurateException {
        Path exampleFilePath = ITEMS_DIR.resolve(EXAMPLE_FILE_NAME);

        try {
            Files.createDirectories(exampleFilePath.getParent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (Files.exists(exampleFilePath)) return;

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(exampleFilePath).build();
        ConfigurationNode rootNode = loader.load();

        ItemConfig exampleConfig = ItemConfig.defaultItemConfig();

        rootNode.set(ItemConfig.class, exampleConfig);
        loader.save(rootNode);
    }

    @Override
    public ItemConfig getConfig(String name) {
        return itemsConfigs.get(name);
    }
}

