package dev.tom.tntWars.services.map;

import dev.tom.tntWars.models.Map;
import dev.tom.tntWars.services.world.WorldManager;

import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class MapManager {

    private final Set<Path> mapPaths;
    private final java.util.Map<String, Map> maps = new HashMap<>();

    public MapManager(){
        mapPaths = getMapPaths();
    }


    private Set<Path> getMapPaths(){
        Set<Path> maps = new HashSet<>();
        File mapDir = WorldManager.getMapTemplatesPath().toFile();
        if(!mapDir.isDirectory()) return maps;
        if(mapDir.listFiles() == null) return maps;
        Set<File> mapFiles = Arrays.stream(mapDir.listFiles()).filter(File::isDirectory).collect(Collectors.toSet());
        return mapFiles.stream().map(File::toPath).collect(Collectors.toSet());
    }

}
