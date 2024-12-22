package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.game.Game;

public interface GameController {

    void startGame(Game game);
    void endGame(Game game);
    void pauseGame(Game game);
    void resumeGame(Game game);
    Game getGameById(String gameId);
}
