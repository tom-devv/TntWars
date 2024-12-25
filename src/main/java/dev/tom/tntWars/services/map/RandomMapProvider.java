package dev.tom.tntWars.services.map;

import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.World;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


public class RandomMapProvider implements MapProvider {

    private final WorldManager worldManager;
    private final List<String> mapNames;

    public RandomMapProvider(WorldManager manager) {
        this.worldManager = manager;
        mapNames = getMapPaths().stream().map(m -> m.getFileName().toString()).collect(Collectors.toList());
    }

    @Override
    public Map getMap() {
        String mapName = pickRandomMapName();
        World world = cloneMapWorld(mapName);
        return null;
    }

    private String pickRandomMapName(){
        int size = getMapNames().size();
        Random random = new Random();
        int index = random.nextInt(size);
        return getMapNames().get(index);
    }

    private Set<Path> getMapPaths(){
        Set<Path> maps = new HashSet<>();
        File mapDir = WorldManager.getMapTemplatesPath().toFile();
        if(!mapDir.isDirectory()) return maps;
        if(mapDir.listFiles() == null) return maps;
        Set<File> mapFiles = Arrays.stream(mapDir.listFiles()).filter(File::isDirectory).collect(Collectors.toSet());
        return mapFiles.stream().map(File::toPath).collect(Collectors.toSet());
    }


    private World cloneMapWorld(String mapName){
        return worldManager.cloneMap(mapName).join();
    }

    public List<String> getMapNames() {
        return mapNames;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
