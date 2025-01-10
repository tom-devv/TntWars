package dev.tom.tntWars.utils;


import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GameUtil {

    public static Optional<Game> getPlayerGame(Player player) {
        return TNTWars.getGameController().getGameByPlayer(player);
    }

}
