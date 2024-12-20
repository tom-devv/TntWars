package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.models.Map;


public class RandomMapProvider extends Controller implements MapController {

    public RandomMapProvider(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public Map getMap() {
        return null;
    }

}
