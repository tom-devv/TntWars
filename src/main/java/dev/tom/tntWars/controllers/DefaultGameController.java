package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;
import dev.tom.tntWars.models.map.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class DefaultGameController extends Controller implements GameController {

    public DefaultGameController(TntWarsPlugin plugin) {
        super(plugin);
    }


    @Override
    public Optional<Game> findGame(GameSettings settings) {
        return Optional.empty();
    }

    @Override
    public Game createGame(Collection<Team> teams, GameSettings settings) {
        return new Game(settings, teams);
    }

    @Override
    public void startGame(Game game) {
        Collection<Team> teams = game.getTeams();
        TntWarsPlugin.getPlugin().getMapController().assignMap(game).thenRun(() -> {
            for (UUID playerUUID : game.getSettings().getTeamProvider().getTeams().get(0).getPlayerUUIDs()) {
                Player player = Bukkit.getPlayer(playerUUID);
                System.out.println(playerUUID);
                player.teleport(game.getMap().getWorld().getSpawnLocation());
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.teleport(new Location(game.getMap().getWorld(), 0, 100, 0));
            }
            System.out.println("Game started with " + teams.size() + " teams.");
            System.out.println("Map: " + game.getMap().getName());
            game.getMap().getTeamSpawnLocations().forEach((team) -> {
                System.out.println("Team " + team.getTeamNumber());
                team.getLocations().forEach((location) -> {
                    System.out.println("Location: " + location.toString());
                });
            });
        });


    }

    private void sendTeamsToGame(Game game){

    }


    @Override
    public void endGame(Game game) {

    }

    @Override
    public void pauseGame(Game game) {

    }

    @Override
    public void resumeGame(Game game) {

    }

    @Override
    public Game getGameById(String gameId) {
        return null;
    }
}
