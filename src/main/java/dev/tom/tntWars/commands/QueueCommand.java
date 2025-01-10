package dev.tom.tntWars.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.services.DefaultMatchmakingService;
import dev.tom.tntWars.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Queue;
import java.util.UUID;

public class QueueCommand {

    public QueueCommand(){
        new CommandAPICommand("queue")
                .withAliases("q")
                .withSubcommands(join(), view(), leave())
                .register();
    }

    public CommandAPICommand join(){
        return new CommandAPICommand("join")
                .withAliases("j")
                .executesPlayer((player, args) -> {
                    DefaultMatchmakingService service = TNTWars.getMatchmakingService();
                    service.addPlayerToQueue(player);
                });
    }

    public CommandAPICommand leave(){
        return new CommandAPICommand("leave")
                .withAliases("l", "q")
                .executesPlayer((player, args) -> {
                    DefaultMatchmakingService service = TNTWars.getMatchmakingService();
                    service.removePlayerFromQueue(player.getUniqueId());
                });
    }

    public CommandAPICommand view(){
        return new CommandAPICommand("view")
                .withAliases("v")
                .executesPlayer((player, args) -> {
                    DefaultMatchmakingService service = TNTWars.getMatchmakingService();
                    Queue<UUID> queue = service.getQueue();
                    StringBuilder sb = new StringBuilder();
                    sb.append("<red><bold>--=Current Queue=--</bold></red>");
                    sb.append("<newline>");
                    queue.forEach(uuid -> {
                        Player queued = Bukkit.getPlayer(uuid);
                        if(queued != null){
                            sb.append("<bold><green>").append(queued.getName()).append("</green></bold><newline>");
                        }
                    });
                    sb.append("<red><bold>-------------------</bold></red>");
                    MessageUtil.sendMini(player, sb.toString());
                });
    }
}
