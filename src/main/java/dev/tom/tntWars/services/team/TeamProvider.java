package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;

import java.util.*;

public abstract class TeamProvider {

    private final int teamCount;

    public TeamProvider(int teamCount){
        this.teamCount = teamCount;
        if(teamCount <= 0) {
            throw new IllegalArgumentException("Team count must be greater than zero.");
        }
    }

    /**
     * For now this should probably just be a constant as I decide a constant number of spawn locations
     * for the current map pool e.g 10 players max, 5 each team
     * @return
     */
    public abstract int maxPlayers();

    public abstract int minimumPlayersRequired();

    public abstract Collection<Team> populateTeams(Collection<UUID> players);

    /**
     * Creates a new set of teams for the current game.
     *
     * @return a list of new teams.
     */
    protected List<Team> createTeams() {
        List<Team> newTeams = new ArrayList<>();
        for (int i = 0; i < this.teamCount; i++) {
            newTeams.add(new Team());
        }
        return newTeams;
    }

    public int getTeamCount() {
        return teamCount;
    }

}
