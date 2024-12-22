package dev.tom.tntWars.events.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinTeamEvent extends TeamEvent{

    private final Player player;

    public PlayerJoinTeamEvent(Team team, Player player) {
        super(team);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
