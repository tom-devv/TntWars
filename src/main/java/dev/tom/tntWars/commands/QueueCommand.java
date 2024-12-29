package dev.tom.tntWars.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.services.DefaultMatchmakingService;

import java.util.List;

public class QueueCommand {

    public QueueCommand(){
        new CommandAPICommand("queue")
                .withAliases("q")
                .withSubcommands(join())
                .register();
    }

    public CommandAPICommand join(){
        return new CommandAPICommand("join")
                .withAliases("j")
                .executesPlayer((player, args) -> {
                    DefaultMatchmakingService service = TntWarsPlugin.getMatchmakingService();
                    service.addPlayerToQueue(player);
                });
    }
}
