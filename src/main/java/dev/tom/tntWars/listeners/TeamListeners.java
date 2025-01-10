package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class TeamListeners implements Listener {


    /**
     * Handle player's leaving while in a game
     * @param e
     */
    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();
        Optional<Game> optionalGame = TNTWars.getGameController().getGameByPlayer(player);
        if(optionalGame.isEmpty()) return;
        Game game = optionalGame.get();
    }
}
