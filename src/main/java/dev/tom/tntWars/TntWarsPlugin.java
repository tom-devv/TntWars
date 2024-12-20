package dev.tom.tntWars;

import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.controllers.DefaultTeamController;
import dev.tom.tntWars.controllers.RandomMapProvider;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.TeamController;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private GameController gameController;
    private TeamController teamController;
    private MapController mapController;

    private static TntWarsPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        initializeDefaults();
        mapController.getMap();
    }


    private void initializeDefaults(){
        WorldManager worldManager = new WorldManager();
        gameController = new DefaultGameController(this);
        teamController = new DefaultTeamController(this);
        mapController = new RandomMapProvider(this, worldManager);
    }


    @Override
    public void onDisable() {

    }

    public static TntWarsPlugin getPlugin() {
        return plugin;
    }

    public GameController getGameController() {
        return gameController;
    }

    public TeamController getTeamController() {
        return teamController;
    }

    public MapController getMapController() {
        return mapController;
    }
}
