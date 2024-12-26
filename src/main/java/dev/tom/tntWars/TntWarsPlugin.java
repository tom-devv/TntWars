package dev.tom.tntWars;

import com.github.javafaker.Faker;
import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.controllers.DefaultMapController;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;


public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private GameController gameController;
    private MapController mapController;

    private static final Faker faker = new Faker();

    private static TntWarsPlugin plugin;

    private static Location lobbyLocation; //TODO: please remove this and make it a config!

    @Override
    public void onEnable() {
        plugin = this;
        loadMapConfigs();
        initializeDefaults();
        deleteThisMethod();
    }

    private void deleteThisMethod(){
        // This method is not used anywhere in the project
        World world = Bukkit.getWorld("world");
        lobbyLocation = new Location(world, 0, 200, 0);
    }

    private void loadMapConfigs(){
        MapConfigLoader mapConfigLoader = new MapConfigLoader(this);
        try {
            mapConfigLoader.loadAll();
        } catch (ConfigurateException e) {
            getLogger().severe("Failed to load map configs: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    private void initializeDefaults(){
        WorldManager worldManager = new WorldManager();

        gameController = new DefaultGameController(this);
        mapController = new DefaultMapController(this);
    }


    @Override
    public void onDisable() {

    }

    public static Faker getFaker() {
        return faker;
    }

    public static TntWarsPlugin getPlugin() {
        return plugin;
    }

    public GameController getGameController() {
        return gameController;
    }

}
