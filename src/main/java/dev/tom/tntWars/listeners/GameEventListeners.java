package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.utils.MapUtils;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import java.util.Optional;

public class GameEventListeners implements Listener {

    @EventHandler
    public void playerExitBounds(PlayerMoveEvent e){
        Player player = e.getPlayer();
        Optional<Game> optionalGame = TntWarsPlugin.getGameController().getGameByPlayer(player);
        if(optionalGame.isEmpty()) return;
        Game game = optionalGame.get();

        // game paused -> freeze movement
        if(game.getState().equals(GameState.PAUSED)) {
            e.setCancelled(true);
            MessageUtil.sendTitle(player.getUniqueId(), "<red><bold>PAUSED!</bold></red>", "<gray>This game is currently paused</gray>");
        }
        // attempting to move outside of map extent or cross centre divide
        Location to = e.getTo();
        if(!MapUtils.withinExtent(game.getMap(), to) || MapUtils.withinCentreDivide(game.getMap(), to) ){
            Vector reboundVelocity = player.getVelocity().multiply(-2);
            player.setVelocity(reboundVelocity);
            e.setCancelled(true);
            MessageUtil.sendTitle(player.getUniqueId(), "<red><bold>Out of bounds!</bold></red>", "<gray>This part of the map is out of bounds</gray>");
            return;
        }
    }

    @EventHandler
    public void friendlyFire(EntityDamageByEntityEvent e){
        // player on player
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player entity){
            Optional<Game> optionalGame = TntWarsPlugin.getGameController().getSharedGame(damager, entity);
            if(optionalGame.isEmpty()) return;
            Game game = optionalGame.get();
            // same team
            if(game.sameTeam(damager, entity)) {
                // prevent friendly fire
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerDeath(PlayerDeathEvent e){

    }


    @EventHandler
    public void gameStart(GameStartEvent event) {
        System.out.println("Game: " + event.getGame() + " has started");
    }

    @EventHandler
    public void gameEnd(GameEndEvent event){

    }
}
