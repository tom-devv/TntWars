package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BalancedTeamProvider extends TeamProvider {


    public BalancedTeamProvider(int teamCount) {
        super(teamCount);
    }

    @Override
    public int minimumPlayersRequired() {
        return 2;
    }

    @Override
    public Collection<Team> populateTeams(Collection<UUID> players) {
        return List.of();
    }



}
