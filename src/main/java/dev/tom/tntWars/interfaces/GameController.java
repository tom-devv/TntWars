package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;

public interface GameController {

    Game createGame(GameSettings settings);
    void startGame(Game game);
    void endGame(Game game);
    void pauseGame(Game game);
    void resumeGame(Game game);
    Game getGameById(String gameId);

}
