package dev.tom.tntWars.events.team;

import dev.tom.tntWars.events.CustomEvent;
import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveTeamEvent extends TeamEvent {

    private final Player player;

    public PlayerLeaveTeamEvent(Team team, Player player) {
        super(team);
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

}
