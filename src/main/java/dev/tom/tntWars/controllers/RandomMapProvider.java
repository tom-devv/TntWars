package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.models.Map;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.World;


public class RandomMapProvider extends Controller implements MapController {

    private final WorldManager worldManager;

    public RandomMapProvider(TntWarsPlugin plugin, WorldManager manager) {
        super(plugin);
        this.worldManager = manager;
    }

    @Override
    public Map getMap() {

        return null;
    }


    private World getMapWorld(){
//        worldManager.cloneMap(world.getName()).thenAccept(w -> {
//            clone = w;
//            System.out.println("Cloned: # " + i + " | " + w.getName());
//        }).exceptionally(e -> {
//            throw new RuntimeException(e);
//        });
        return null;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
