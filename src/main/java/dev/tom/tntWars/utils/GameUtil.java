package dev.tom.tntWars.utils;


import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.controllers.DefaultGameController;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GameUtil {

    public static Optional<Game> getPlayerGame(Player player) {
        return TNTWars.getGameController().getGameByPlayer(player);
    }

    public static Optional<Game> getLocationGame(Location location){
        if(TNTWars.getGameController() instanceof DefaultGameController controller){
            Game game = controller.getWorldToGame().get(location.getWorld());
            if(game == null) return Optional.empty();
            return Optional.of(game);
        }
        return Optional.empty();
    }

}
