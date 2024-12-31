package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.models.game.GameStats;
import dev.tom.tntWars.utils.GameUtil;
import dev.tom.tntWars.utils.MapUtils;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
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
        GameUtil.getPlayerGame(player).ifPresent(game-> {
            // game paused -> freeze movement
            if (game.getState().equals(GameState.PAUSED)) {
                e.setCancelled(true);
                MessageUtil.sendTitle(player.getUniqueId(), "<red><bold>PAUSED!</bold></red>", "<gray>This game is currently paused</gray>");
            }
            // attempting to move outside of map extent or cross centre divide
            Location to = e.getTo();
            if (!MapUtils.withinExtent(game.getMap(), to) || MapUtils.withinCentreDivide(game.getMap(), to)) {
                Vector reboundVelocity = player.getVelocity().multiply(-2);
                player.setVelocity(reboundVelocity);
                e.setCancelled(true);
                MessageUtil.sendTitle(player.getUniqueId(), "<red><bold>Out of bounds!</bold></red>", "<gray>This part of the map is out of bounds</gray>");
                return;
            }
        });
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

    /**
     * This even it specifically for when a player is KILLED
     * rather than when a player dies, being killed can only
     * occur if you die by TNT from the enemy team exploding
     * and dealing enough damage to kill you
     * @param e
     */
    @EventHandler
    public void playerKilled(PlayerDeathEvent e){
        Player player = e.getPlayer();
        DamageSource source = e.getDamageSource();
        if(!(source.getDirectEntity() instanceof TNTPrimed tnt)) return;
        GameUtil.getPlayerGame(player).ifPresent(game->{
            int teamNumber = MapUtils.getTeamRegion(game.getMap(), tnt.getOrigin());
            if(teamNumber == -1) throw new RuntimeException("Player: " + player.getName() + " died to an explosion fired from outside any team region: " + tnt.getOrigin());
            Team teamResponsible = game.getTeam(teamNumber);
            GameStats stats = game.getStats();
            int teamKills = stats.addTeamKill(teamResponsible);
            // team out of lives
            if(teamKills > game.getSettings().getLivesPerTeam()) {
                game.getStats().setWinningTeam(teamResponsible);
                TntWarsPlugin.getGameController().endGame(game);
            }
        });

    }


    @EventHandler
    public void gameStart(GameStartEvent event) {
        System.out.println("Game: " + event.getGame() + " has started");
    }

    @EventHandler
    public void gameEnd(GameEndEvent event){
        System.out.println("Game: " + event.getGame() + " has ended");
    }
}
