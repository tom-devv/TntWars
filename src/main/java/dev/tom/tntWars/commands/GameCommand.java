package dev.tom.tntWars.commands;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.interfaces.GameController;
import dev.tom.tntWars.services.DefaultMatchmakingService;
import dev.tom.tntWars.utils.GameUtil;
import org.bukkit.entity.Player;

public class GameCommand {

    public GameCommand(){
        new CommandAPICommand("game")
                .withAliases("g")
                .withSubcommands(end(), pause(), resume())
                .register();
    }

    public CommandAPICommand end(){
        return new CommandAPICommand("end")
                .withAliases("stop")
                .withRequirement(sender -> new GameWorldRequirement().test(sender))
                .executesPlayer((player, args) -> {
                    GameUtil.getLocationGame(player.getLocation()).ifPresent(game -> {
                        GameController controller = TNTWars.getGameController();
                        controller.endGame(game);
                    });
                });
    }

    public CommandAPICommand pause(){
        return new CommandAPICommand("pause")
                .withRequirement(sender -> new GameWorldRequirement().test(sender))
                .executesPlayer((player, args) -> {
                    GameUtil.getLocationGame(player.getLocation()).ifPresent(game -> {
                        GameController controller = TNTWars.getGameController();
                        controller.pauseGame(game);
                    });
                });
    }

    public CommandAPICommand resume(){
        return new CommandAPICommand("resume")
                .withRequirement(sender -> new GameWorldRequirement().test(sender))
                .executesPlayer((player, args) -> {
                    GameUtil.getLocationGame(player.getLocation()).ifPresent(game -> {
                        GameController controller = TNTWars.getGameController();
                        controller.resumeGame(game);
                    });
                });
    }

}
