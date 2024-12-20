package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.models.Game;

public class DefaultGameController extends Controller implements GameController {

    public DefaultGameController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void startGame(Game game) {

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
