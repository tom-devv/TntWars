package dev.tom.tntWars.commands;

import dev.tom.tntWars.utils.GameUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Predicate;

public class GameWorldRequirement implements Predicate<CommandSender> {

    @Override
    public boolean test(CommandSender sender) {
        if(sender instanceof Player player) return GameUtil.getLocationGame(player.getLocation()).isPresent();
        return false;
    }
}
