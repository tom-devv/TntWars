package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.config.map.MapConfig;
import dev.tom.tntWars.events.game.GamePlayerDeathEvent;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameState;
import dev.tom.tntWars.models.game.GameStats;
import dev.tom.tntWars.utils.GameUtil;
import dev.tom.tntWars.utils.MapUtils;
import dev.tom.tntWars.utils.MessageUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
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
            TNTWars.getGameController().removePlayer(game, player);
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
            // attempting to cross centre divide
            Location to = e.getTo();
            if (MapUtils.withinCentreDivide(game.getMap(), to)) {
                e.setCancelled(true);
                MessageUtil.sendTitle(player.getUniqueId(), "<red><bold>Out of bounds!</bold></red>", "<gray>This part of the map is out of bounds</gray>");
            }
        });
    }

    @EventHandler
    public void friendlyFire(EntityDamageByEntityEvent e){
        // player on player
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player entity){
            Optional<Game> optionalGame = TNTWars.getGameController().getSharedGame(damager, entity);
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
        e.deathMessage(Component.text("")); // remove default death message
        Player deadPlayer = e.getPlayer();
        DamageSource source = e.getDamageSource();
        GameUtil.getPlayerGame(deadPlayer).ifPresent(game->{
            // player has been killed so update stats of killer team
            GamePlayerDeathEvent deathEvent;
            if(source.getDirectEntity() instanceof TNTPrimed tnt) {
                int teamNumber = MapUtils.getTeamRegion(game.getMap(), tnt.getOrigin());
                if(teamNumber == -1) throw new RuntimeException("Player: " + deadPlayer.getName() + " died to an explosion fired from outside any team region: " + tnt.getOrigin());
                Team killerTeam = game.getTeam(MapUtils.getTeamRegion(game.getMap(), tnt.getOrigin()));
                if(killerTeam == null) throw new RuntimeException("Failed to get team for location: " + tnt.getOrigin() + " in game: " + game.getGameId());
                game.getStats().addTeamKill(killerTeam);
                deathEvent = new GamePlayerDeathEvent(game, deadPlayer, killerTeam);
                //TODO handle individual player kill stats using dispenser nbt
            } else {
               deathEvent = new GamePlayerDeathEvent(game, deadPlayer, null);
            }
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
        /**
         * Update stats
         */
        Team deadPlayerTeam = optionalTeam.get();
        GameStats stats = game.getStats();
        stats.addTeamDeath(deadPlayerTeam);
        /**
         * Respawn
         */
        int livesLeft = game.getSettings().getLivesPerTeam() - stats.getTeamDeaths().get(deadPlayerTeam);
        if(livesLeft > 0) {
            TNTWars.getGameController().respawnPlayer(game, deadPlayer);
            MessageUtil.sendActionBar(deadPlayer, "<green><bold>Respawned!<reset> <gray>|</gray> <red>" + livesLeft + " lives remaining</red></green>");
        }
        /**
         * Death message
         */
        String killersAppend = e.getKillers() == null ? "<red><bold>has died</red></bold>" : "has been <red><bold>exploded</bold> <red>by Team: " + e.getKillers().getNumber() + "</red>";
        String deathMessage = "<green><bold>" + deadPlayer.getName() + "</bold> " + killersAppend + " </green>" +
                "<newline>" +
                "<green>Team: <red>" + deadPlayerTeam.getNumber() + "</red> has <red><bold>" + livesLeft + "</bold><red> lives remaining</green>";
        game.applyPlayers(player -> {
            MessageUtil.sendMini(player, deathMessage);
        });
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
        // todo refactor this so below error doesn't happen
        // all but one teams are dead so this team is the winner and the game is over
        // slight logic error here, if there is only one team playing then upon death
        // the game will end as there is only one team left
        if(aliveTeams.size() == 1){
            stats.setWinningTeam(aliveTeams.getFirst());
            TNTWars.getGameController().endGame(game);
        }
    }

    @EventHandler
    public void gameItemUse(PlayerItemDamageEvent e){
        GameUtil.getPlayerGame(e.getPlayer()).ifPresent(game->{
           e.setCancelled(true);  // stop item damage in game //todo maybe not armour too though? armour?
        });
    }

    @EventHandler
    public void blockPlace(BlockPlaceEvent e){
        GameUtil.getPlayerGame(e.getPlayer()).ifPresent(game-> {
            Player player = e.getPlayer();

            // replenish placed blocks, infinite now!
            ItemStack item = e.getItemInHand();
            item.setAmount(item.getAmount());
            player.getInventory().setItemInMainHand(item);

            //prevent placing outside of team territory
            Location where = e.getBlock().getLocation();
            game.getTeam(player).ifPresent(team -> {
                boolean within = MapUtils.withinTeamRegion(where, game.getMap(), team);
                if(!within) e.setCancelled(true);
            });
        });
    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e) {
        GameUtil.getPlayerGame(e.getPlayer()).ifPresent(game-> {
            Location where = e.getBlock().getLocation();
            game.getTeam(e.getPlayer()).ifPresent(team -> {
                boolean within = MapUtils.withinTeamRegion(where, game.getMap(), team);
                if (!within) e.setCancelled(true);
            });
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
