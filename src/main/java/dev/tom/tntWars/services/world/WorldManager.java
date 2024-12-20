package dev.tom.tntWars.services.world;

import dev.tom.tntWars.TntWarsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class WorldManager {

    private static final Path CLONED_MAPS_PATH = getClonedMapsPath();

    private static final Path MAP_TEMPLATES_PATH = getMapTemplatesPath();


    // create clone world directory if it doesn't exist
    static {
        if(Files.notExists(CLONED_MAPS_PATH)){
            try {
                Files.createDirectories(CLONED_MAPS_PATH);
            } catch (IOException e) {
                TntWarsPlugin.getPlugin(TntWarsPlugin.class).getLogger().severe("Failed to create cloned maps directory: " + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        if(Files.notExists(MAP_TEMPLATES_PATH)){
            try {
                Files.createDirectories(MAP_TEMPLATES_PATH);
            } catch (IOException e) {
                TntWarsPlugin.getPlugin(TntWarsPlugin.class).getLogger().severe("Failed to create map templates directory: " + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private static final List<String> IGNORED_FILES = Arrays.asList("uid.dat", "session.lock");

    public CompletableFuture<World> cloneMap(World template) {
        if (template == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Map not found"));
        }
        String newName = template.getName()+ "_" + UUID.randomUUID();
        return cloneWorld(template, newName);
    }

    public CompletableFuture<World> cloneMap(String mapName) {
        World world = Bukkit.getWorld(mapName);
        if (world == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Map not found"));
        }
        String newName = mapName + "_" + UUID.randomUUID();
        return cloneWorld(world, newName);
    }

    private CompletableFuture<World> cloneWorld(World world, String newName){
        Path source = world.getWorldFolder().toPath();
        Path target = getClonedMapsPath().resolve(newName);

        // Async clone the directory
        return CompletableFuture.supplyAsync(() -> {
            copyFolder(source, target);
            return target;
        }).thenComposeAsync(targetPath -> CompletableFuture.supplyAsync(() -> {
            try {
                // Sync create the world and return a future
                return Bukkit.getScheduler().callSyncMethod(TntWarsPlugin.getPlugin(), () -> {
                    System.out.println("Cloned world files to: " + targetPath.toString() + "\nCreating world creator now...");
                    WorldCreator worldCreator = new WorldCreator(targetPath.toString());
                    worldCreator.generator(new EmptyChunkGenerator());
                    worldCreator.generateStructures(false);
                    worldCreator.type(WorldType.FLAT);
                    return worldCreator.createWorld();
                }).get(); // blocks main thread waiting for future to complete
            } catch (InterruptedException | ExecutionException e) {
                TntWarsPlugin.getPlugin(TntWarsPlugin.class).getLogger().severe("Failed to clone world: " + e.getMessage());
                throw new RuntimeException(e);
            }
        })
        .exceptionally(e -> {
            TntWarsPlugin.getPlugin(TntWarsPlugin.class).getLogger().severe("Failed to clone world: " + e.getMessage());
            throw new RuntimeException(e);
        })
        );
    }

    public void copyFolder(Path src, Path dest) {
        try {
            try (Stream<Path> stream = Files.walk(src)) {
                stream.forEach(source -> {
                    if (IGNORED_FILES.contains(source.getFileName().toString())) return;
                    copy(source, dest.resolve(src.relativize(source)));
                });
            }
        }catch (IOException e){
            TntWarsPlugin.getPlugin(TntWarsPlugin.class).getLogger().severe("Failed to copy world: " + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void copy(Path source, Path dest) {
        try {
            Files.copy(source, dest, REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Path getClonedMapsPath() {
        File pluginDataFolder = TntWarsPlugin.getPlugin().getDataFolder();
        return pluginDataFolder.toPath().resolve("cloned_maps"); // This resolves to plugin folder/cloned_maps
    }

    public static Path getMapTemplatesPath() {
        File pluginDataFolder = TntWarsPlugin.getPlugin().getDataFolder();
        return pluginDataFolder.toPath().resolve("map_templates");
    }
}
