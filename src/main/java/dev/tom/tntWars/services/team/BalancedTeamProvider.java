package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BalancedTeamProvider extends TeamProvider {

    public BalancedTeamProvider(int teamCount) {
        super(teamCount);
    }

    @Override
    public int maxPlayers() {
        return 10;
    }

    @Override
    public int minimumPlayersRequired() {
        return getTeamCount();
    }

    @Override
    public Collection<Team> populateTeams(Collection<UUID> players) {
        int splits = getTeamCount();
        List<Team> teams = createTeams();
        List<List<UUID>> split = splitCollection(players, splits);

        // assign players to team
        for (int i = 0; i < split.size(); i++) {
            List<UUID> part = split.get(i);
            Team team = teams.get(i);
            for (UUID uuid : part) {
                System.out.println("Adding: " + uuid + " to team: " + team + " number: " + i);
            }
            part.forEach(team::addPlayer);
        }
        return teams;
    }

    /**
     * Splits a collection into n roughly equal-sized sublists.
     *
     * @param collection the collection to split
     * @param n          the number of partitions
     * @param <T>        the type of elements in the collection
     * @return a list of n partitions
     */
    public static <T> List<List<T>> splitCollection(Collection<T> collection, int n) {
        List<T> list = new ArrayList<>(collection);

        List<List<T>> partitions = new ArrayList<>();
        int totalSize = list.size();
        int partitionSize = (int) Math.ceil((double) totalSize / n);

        for (int i = 0; i < n; i++) {
            int start = i * partitionSize;
            int end = Math.min(start + partitionSize, totalSize);
            if (start < totalSize) {
                partitions.add(new ArrayList<>(list.subList(start, end)));
            }
        }

        return partitions;
    }
}
