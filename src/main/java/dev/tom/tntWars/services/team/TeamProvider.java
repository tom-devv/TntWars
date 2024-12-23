package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;

import java.util.*;

public abstract class TeamProvider {

    private final int teamCount;
    private final List<Team> teams = new ArrayList<>();
    private final Collection<UUID> players;

    public TeamProvider(Collection<UUID> players, int teamCount){
        this.players = players;
        this.teamCount = teamCount;
        initializeTeams();
        populateTeams();
        if(teams.size() != teamCount) {
            throw new RuntimeException("Team count does not match the number of teams created.");
        }
    }

    public abstract int minimumPlayersRequired();

    public abstract void populateTeams();

    public void initializeTeams(){
        for(int i = 0; i < this.teamCount; i++){
            teams.add(new Team());
        }
    }

    public void clearTeams(){
        teams.forEach(Team::clearTeam);
    }

    public int getTeamCount() {
        return teamCount;
    }

    public Collection<UUID> getPlayers() {
        return players;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
