package dev.tom.tntWars.utils;


import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GameUtil {

    public static Optional<Game> getPlayerGame(Player player) {
        return TntWarsPlugin.getGameController().getGameByPlayer(player);
    }

}
