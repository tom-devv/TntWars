package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class RandomTeamProvider extends TeamProvider {

    public RandomTeamProvider(Collection<UUID> players, int teamCount) {
        super(players, teamCount);
    }

    @Override
    public void populateTeams() {
        clearTeams();
        for (UUID uuid : getPlayers()) {
            Team team = getTeams().get((int) (Math.random() * getTeams().size()));
            team.addPlayer(uuid);
        }
    }

    @Override
    public int minimumPlayersRequired() {
        return 4;
    }
}
