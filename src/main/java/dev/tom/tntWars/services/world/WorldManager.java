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
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class WorldManager {

    private static final List<String> IGNORED_FILES = Arrays.asList("uid.dat", "session.lock");

    public CompletableFuture<World> cloneMap(String mapName) {
        World world = Bukkit.getWorld(mapName);
        if (world == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Map not found"));
        }
        String newName = mapName + "_" + UUID.randomUUID();
        return cloneWorld(world, newName);
    }

    private CompletableFuture<World> cloneWorld(World world, String newName) {
        Path source = world.getWorldFolder().toPath();
        Path target = Paths.get("").resolve(newName); // "/newName

        return CompletableFuture.supplyAsync(() -> {
            copyFolder(source, target);
            return target;
        }).thenCompose(targetPath -> {
            WorldCreator worldCreator = new WorldCreator(newName);
            worldCreator.generator(new EmptyChunkGenerator());
            worldCreator.generateStructures(false);
            worldCreator.type(WorldType.FLAT);
            World clonedWorld = worldCreator.createWorld();
            return CompletableFuture.completedFuture(clonedWorld);
        });
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

    public static Path getTemplateDir() {
        return TEMPLATE_DIR;
    }
}
