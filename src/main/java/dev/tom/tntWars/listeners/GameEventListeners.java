package dev.tom.tntWars.listeners;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.events.game.GameEndEvent;
import dev.tom.tntWars.events.game.GameStartEvent;
import dev.tom.tntWars.models.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.swing.text.html.Option;
import java.util.Optional;

public class GameEventListeners implements Listener {

    @EventHandler
    public void handleFriendlyFire(EntityDamageByEntityEvent e){
        // player on player
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof Player entity){
            Optional<Game> optionalGame = TntWarsPlugin.getGameController().getSharedGame(damager, entity);
            if(optionalGame.isEmpty()) return;
            Game game = optionalGame.get();

            // players are in the same game
            if(!game.sameTeam(damager, entity)) {

            }
            // same team
            else {
                // prevent friendly fire
                e.setCancelled(true);
            }

        }
    }


    @EventHandler
    public void gameStart(GameStartEvent event) {
        System.out.println("Game: " + event.getGame() + " has started");
    }

    @EventHandler
    public void gameEnd(GameEndEvent event){

    }
}
