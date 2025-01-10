package dev.tom.tntWars.services.world;

import dev.tom.tntWars.TNTWars;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.codehaus.plexus.util.FileUtils;

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

    static {
        if(Files.notExists(CLONED_MAPS_PATH)){
            try {
                Files.createDirectories(CLONED_MAPS_PATH);
            } catch (IOException e) {
                TNTWars.getPlugin(TNTWars.class).getLogger().severe("Failed to create cloned maps directory: " + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        if(Files.notExists(MAP_TEMPLATES_PATH)){
            try {
                Files.createDirectories(MAP_TEMPLATES_PATH);
            } catch (IOException e) {
                TNTWars.getPlugin(TNTWars.class).getLogger().severe("Failed to create map templates directory: " + e.getMessage());
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    private static final List<String> IGNORED_FILES = Arrays.asList("uid.dat", "session.lock");

    public CompletableFuture<World> cloneMap(String mapName) {
        String newName = mapName + "_" + UUID.randomUUID();
        Path mapPath = MAP_TEMPLATES_PATH.resolve(mapName);
        System.out.println("Map Path: " + mapPath);
        return cloneWorld(mapPath, newName);
    }

    private CompletableFuture<World> cloneWorld(Path source, String newName){
        Path target = getClonedMapsPath().resolve(newName);

        // Async clone the directory
        return CompletableFuture.supplyAsync(() -> {
            copyFolder(source, target);
            return target;
        }).thenComposeAsync(targetPath -> CompletableFuture.supplyAsync(() -> {
            try {
                // Sync create the world and return a future
                return Bukkit.getScheduler().callSyncMethod(TNTWars.getPlugin(), () -> {
                    System.out.println("Cloned world files to: " + targetPath.toString() + "\nCreating world creator now...");
                    WorldCreator worldCreator = new WorldCreator(targetPath.toString());
                    worldCreator.generator(new EmptyChunkGenerator());
                    worldCreator.generateStructures(false);
                    worldCreator.type(WorldType.FLAT);
                    return worldCreator.createWorld();
                }).get(); // blocks main thread waiting for future to complete
            } catch (InterruptedException | ExecutionException e) {
                TNTWars.getPlugin(TNTWars.class).getLogger().severe("Failed to clone world 1 : " + e.getMessage());
                throw new RuntimeException(e);
            }
        })
        .exceptionally(e -> {
            TNTWars.getPlugin(TNTWars.class).getLogger().severe("Failed to clone world 2: " + e.getMessage());
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
            TNTWars.getPlugin(TNTWars.class).getLogger().severe("Failed to copy world: " + e.getMessage());
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

    public void deleteWorld(World world) {
        System.out.println("Deleting world");
        File worldFile = world.getWorldFolder();
        boolean unload = Bukkit.unloadWorld(world, false);
        System.out.println("Unload World? " + unload);
        try {
            FileUtils.deleteDirectory(worldFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Path getClonedMapsPath() {
        File pluginDataFolder = TNTWars.getPlugin().getDataFolder();
        return pluginDataFolder.toPath().resolve("cloned_maps"); // This resolves to plugin folder/cloned_maps
    }

    public static Path getMapTemplatesPath() {
        File pluginDataFolder = TNTWars.getPlugin().getDataFolder();
        return pluginDataFolder.toPath().resolve("map_templates");
    }
}
