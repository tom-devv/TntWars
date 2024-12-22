package dev.tom.tntWars.events.team;

import dev.tom.tntWars.events.CustomEvent;
import dev.tom.tntWars.models.Team;

public abstract class TeamEvent extends CustomEvent {

    private final Team team;

    public TeamEvent(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }
}
