package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RandomTeamProvider extends TeamProvider {

    public RandomTeamProvider (int teamCount) {
        super(teamCount);
    }

    @Override
    public Collection<Team> populateTeams(Collection<UUID> players) {
        clearTeams();
        for (UUID uuid : players) {
            Team team = getTeams().get((int) (Math.random() * getTeams().size()));
            team.addPlayer(uuid);
        }
        return getTeams();
    }

    @Override
    public int minimumPlayersRequired() {
        return 1;
    }


}
