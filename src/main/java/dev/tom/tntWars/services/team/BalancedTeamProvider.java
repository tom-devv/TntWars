package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
        int splits = getTeamCount();

        // split uuids evenly into lists
        List<List<UUID>> split = splitCollection(players, splits);

        // assign each player to a team
        for (int i = 0; i < split.size(); i++) {
            List<UUID> part = split.get(i);
            Team team = getTeams().get(i);
            part.forEach(team::addPlayer);
        }
        return getTeams();
    }


    public static <T> List<List<T>> splitCollection(Collection<T> collection, int n) {

        List<T> list = new ArrayList<>(collection);

        List<List<T>> partitions = new ArrayList<>();
        int totalSize = list.size();
        int partitionSize = (int) Math.ceil((double) totalSize / n);

        for (int i = 0; i < totalSize; i += partitionSize) {
            int end = Math.min(i + partitionSize, totalSize);
            partitions.add(new ArrayList<>(list.subList(i, end)));
        }

        return partitions;
    }



}
