package dev.tom.tntWars;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.controllers.DefaultTeamController;
import dev.tom.tntWars.controllers.RandomMapProvider;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.interfaces.MapController;
import dev.tom.tntWars.interfaces.TeamController;
import dev.tom.tntWars.services.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;


public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private GameController gameController;
    private TeamController teamController;
    private MapController mapController;

    private static TntWarsPlugin plugin;

    @Override
    public void onEnable() {
        plugin = this;
        initializeDefaultControllers();
    }





    private void initializeDefaultControllers(){
        gameController = new DefaultGameController(this);
        teamController = new DefaultTeamController(this);
        mapController = new RandomMapProvider(this);
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
