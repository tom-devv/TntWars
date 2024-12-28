package dev.tom.tntWars;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.javafaker.Faker;
import dev.tom.tntWars.config.MapConfigLoader;
import dev.tom.tntWars.config.MapSpawnsConfig;
import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.controllers.DefaultMapController;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.services.DefaultMatchmakingService;
import dev.tom.tntWars.services.map.RandomMapProvider;
import dev.tom.tntWars.services.team.RandomTeamProvider;
import dev.tom.tntWars.services.team.TeamProvider;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;


public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private GameController gameController;
    private MapController mapController;

    private DefaultMatchmakingService matchmakingService;

    private static MapProvider randomMapProvider;
    private static TeamProvider randomTeamProvider;

    private static final Faker faker = new Faker();

    private static TntWarsPlugin plugin;

    private static Location lobbyLocation; //TODO: please remove this and make it a config!

    @Override
    public void onEnable() {
        plugin = this;
        loadMapConfigs();
        initializeDefaults();
        deleteThisMethod();
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void jump(PlayerJumpEvent e){
        matchmakingService.addPlayerToQueue(e.getPlayer());
        getLogger().info("Player " + e.getPlayer().getName() + " has joined the queue.");
    }

    @EventHandler
    public void sneak(PlayerToggleSneakEvent e){
        matchmakingService.startGameWithTeams(randomTeamProvider);
        getLogger().info("Game started with teams.");
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


        randomMapProvider = new RandomMapProvider(worldManager);
        randomTeamProvider = new RandomTeamProvider(1);

        matchmakingService = new DefaultMatchmakingService(GameSettings.defaultSettings());

    }


    @Override
    public void onDisable() {

    }


    public static MapProvider getRandomMapProvider() {
        return randomMapProvider;
    }

    public MapController getMapController() {
        return mapController;
    }

    public DefaultMatchmakingService getMatchmakingService() {
        return matchmakingService;
    }

    public static TeamProvider getRandomTeamProvider() {
        return randomTeamProvider;
    }

    public static Location getLobbyLocation() {
        return lobbyLocation;
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
