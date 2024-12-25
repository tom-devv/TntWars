package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.models.game.Game;
import dev.tom.tntWars.models.game.GameSettings;

import java.util.Collection;
import java.util.Optional;

public interface GameController {

    Optional<Game> findGame(GameSettings settings);
    Game createGame(Collection<Team> teams, GameSettings settings);
    void startGame(Game game);
    void endGame(Game game);
    void pauseGame(Game game);
    void resumeGame(Game game);
    Game getGameById(String gameId);

}
