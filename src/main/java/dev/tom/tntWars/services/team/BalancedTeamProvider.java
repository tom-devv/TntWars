package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;

import java.util.Collection;
import java.util.UUID;

public class BalancedTeamProvider extends TeamProvider {

    public BalancedTeamProvider(Collection<UUID> players, int teamCount) {
        super(players, teamCount);
    }

    @Override
    public int minimumPlayersRequired() {
        return 2;
    }

    @Override
    public Collection<Team> populateTeams() {
        return null; // TODO imp
    }
}
