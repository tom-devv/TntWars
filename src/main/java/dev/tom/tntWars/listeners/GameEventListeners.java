package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.events.game.GamePlayerDeathEvent;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.models.game.GameStats;
import dev.tom.tntWars.utils.GameUtil;
import dev.tom.tntWars.utils.MapUtils;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class GameEventListeners implements Listener {

    /**
     * Gracefully remove a player from a game
     * @param event
     */
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GameUtil.getPlayerGame(player).ifPresent(game -> {
            TntWarsPlugin.getGameController().removePlayer(game, player);
        });
    }

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

    @EventHandler
    public void playerDeath(PlayerDeathEvent e){
        Player deadPlayer = e.getPlayer();
        DamageSource source = e.getDamageSource();
        GameUtil.getPlayerGame(deadPlayer).ifPresent(game->{
            // player has been killed so update stats of killer team
            if(source.getDirectEntity() instanceof TNTPrimed tnt) {
                int teamNumber = MapUtils.getTeamRegion(game.getMap(), tnt.getOrigin());
                if(teamNumber == -1) throw new RuntimeException("Player: " + deadPlayer.getName() + " died to an explosion fired from outside any team region: " + tnt.getOrigin());
                Team killerTeam = game.getTeam(MapUtils.getTeamRegion(game.getMap(), tnt.getOrigin()));
                if(killerTeam == null) throw new RuntimeException("Failed to get team for location: " + tnt.getOrigin() + " in game: " + game.getGameId());
                game.getStats().addTeamKill(killerTeam);
                //TODO handle individual player kill stats using dispenser nbt
            }
            GamePlayerDeathEvent deathEvent = new GamePlayerDeathEvent(game, deadPlayer);
            Bukkit.getPluginManager().callEvent(deathEvent);
        });
    }

    /**
     * Handles logic for players dying, by adding a death to their team
     * after this we determine if the game should end
     * @param e
     */
    @EventHandler
    public void playerKilled(GamePlayerDeathEvent e){
        Game game = e.getGame();
        Player deadPlayer = e.getDead();
        Optional<Team> optionalTeam = game.getTeam(deadPlayer);
        if(optionalTeam.isEmpty()) {
            throw new RuntimeException("Player: " + deadPlayer + " died but isn't in a team in game: " + game.getGameId());
        }
        // Should the player be respawned?
        Team deadPlayerTeam = optionalTeam.get();
        GameStats stats = game.getStats();
        stats.addTeamDeath(deadPlayerTeam);
        int livesLeft = game.getSettings().getLivesPerTeam() - stats.getTeamDeaths().get(deadPlayerTeam);
        if(livesLeft > 0) {
//            deadPlayer.spigot().respawn(); // force a respawn to skip title screen
            TntWarsPlugin.getGameController().respawnPlayer(game, deadPlayer);
            MessageUtil.sendTitle(deadPlayer,
                    "<green><bold>Respawned!",
                    "<gray>Your team now has <red>" + livesLeft + "</red> lives remaining</gray>"
            );
        }

        handleGameDeaths(game);
    }

    /**
     * Determine if the game should be ended and set a winner
     * @param game
     */
    private void handleGameDeaths(Game game){
        GameStats stats = game.getStats();
        List<Team> aliveTeams = new ArrayList<>(game.getTeams()); // assume all teams are alive
        for (Team team : game.getTeams()) {
            Integer teamDeaths = stats.getTeamDeaths().get(team);
            if(teamDeaths == null) continue;

            // this team has reached the max deaths
            if(teamDeaths == game.getSettings().getLivesPerTeam()) {
                aliveTeams.remove(team);
            }
        }
        // all but one teams are dead so this team is the winner and the game is over
        if(aliveTeams.size() == 1){
            stats.setWinningTeam(aliveTeams.getFirst());
            TntWarsPlugin.getGameController().endGame(game);
        }
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
