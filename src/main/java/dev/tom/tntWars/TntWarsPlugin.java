package dev.tom.tntWars;

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
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;

import java.io.IOException;

public final class TntWarsPlugin extends JavaPlugin implements Listener {

    private GameController gameController;
    private TeamController teamController;
    private MapController mapController;

    public static World clone;

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

        WorldManager manager = new WorldManager();
        World world = Bukkit.getWorld("world");
        manager.cloneWorld(world).thenAcceptAsync(w -> {
            clone = w;
        });


    }



    @EventHandler
    public void toggleSneak(PlayerBucketEmptyEvent e){
        System.out.println(
                "Player " + e.getPlayer().getName() + " toggled sneak"
        );
        e.getPlayer().teleport(new Location(clone, 0, 100, 0));
    }


    private void initializeDefaultControllers(){
        gameController = new DefaultGameController(this);
        teamController = new DefaultTeamController(this);
        mapController = new RandomMapProvider(this);
    }


    @Override
    public void onDisable() {

    }
}
