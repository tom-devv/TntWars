package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;

import java.util.Collection;
import java.util.Optional;

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
