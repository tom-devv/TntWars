package dev.tom.tntWars.services.team;

import dev.tom.tntWars.models.Team;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class TeamProvider {

    private final int teamCount;
    private final List<Team> teams = new ArrayList<>();

    public TeamProvider(int teamCount){
        this.teamCount = teamCount;
        initializeTeams();
        if(teams.size() != teamCount) {
            throw new RuntimeException("Team count does not match the number of teams created.");
        }
    }

    public abstract int minimumPlayersRequired();

    public abstract Collection<Team> populateTeams(Collection<UUID> players);

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


    public List<Team> getTeams() {
        return teams;
    }
}
