package dev.tom.tntWars;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.github.javafaker.Faker;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.tom.tntWars.commands.QueueCommand;
import dev.tom.tntWars.config.MapConfigLoader;
import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.controllers.DefaultMapController;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.listeners.GameEventListeners;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.placeholders.TntPlaceholders;
import dev.tom.tntWars.services.DefaultMatchmakingService;
import dev.tom.tntWars.services.map.RandomMapProvider;
import dev.tom.tntWars.services.team.BalancedTeamProvider;
import dev.tom.tntWars.services.team.TeamProvider;
import dev.tom.tntWars.services.world.WorldManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;


public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private static GameController gameController;
    private static MapController mapController;

    private static DefaultMatchmakingService matchmakingService;

    private static MapProvider randomMapProvider;
    private static TeamProvider balancedTeamProvider;
    private static WorldManager worldManager;

    private static final Faker faker = new Faker();

    private static TntWarsPlugin plugin;

    private static Location lobbyLocation; //TODO: please remove this and make it a config!

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this).verboseOutput(true)); // Load with verbose output
    }

    @Override
    public void onEnable() {
        plugin = this;
        CommandAPI.onEnable();
        placeholderAPI();
        loadCommands();
        loadMapConfigs();
        initializeDefaults();
        deleteThisMethod();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new GameEventListeners(), this);
    }

    @EventHandler
    public void jump(PlayerJumpEvent e){
        if(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BONE)){
            matchmakingService.addPlayerToQueue(e.getPlayer());
        }
    }

    private void loadCommands(){
        new QueueCommand();
    }

    /**
     * PlaceholderAPI is a soft dependency so we only register them if
     * placeholderapi is found
     */
    private void placeholderAPI(){
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TntPlaceholders().register();
        }
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
        worldManager = new WorldManager();
        gameController = new DefaultGameController(this);
        mapController = new DefaultMapController(this);


        randomMapProvider = new RandomMapProvider(worldManager);
        balancedTeamProvider = new BalancedTeamProvider(1);

        matchmakingService = new DefaultMatchmakingService(GameSettings.defaultSettings());

    }


    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }


    public static MapProvider getRandomMapProvider() {
        return randomMapProvider;
    }

    public static DefaultMatchmakingService getMatchmakingService() {
        return matchmakingService;
    }

    public static TeamProvider getBalancedTeamProvider() {
        return balancedTeamProvider;
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

    public static GameController getGameController() {
        return gameController;
    }

    public static MapController getMapController() {
        return mapController;
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }
}
