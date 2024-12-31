package dev.tom.tntWars.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class MessageUtil {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    // Method to send a colored message to a player using MiniMessage
    public static void sendMini(Player player, String message) {
        Component component = miniMessage.deserialize(message);
        player.sendMessage(component);
    }

    public static void sendMini(UUID uuid, String message){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) {
            throw new IllegalStateException("Tried to send message to: `" + uuid + "` but failed because the player could not be fetched");
        }
        sendMini(player, message);
    }

    public static void sendTitle(Player player, String main, String sub) {
        Title title = Title.title(miniMessage.deserialize(main), miniMessage.deserialize(sub));
        player.showTitle(title);
    }

    public static void sendTitle(UUID uuid, String main, String sub){
        Player player = Bukkit.getPlayer(uuid);
        if(player == null) {
            throw new IllegalStateException("Tried to send title to: `" + uuid + "` but failed because the player could not be fetched");
        }
        sendTitle(player, main, sub);
    }

}
