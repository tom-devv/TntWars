package dev.tom.tntWars.interfaces;

import dev.tom.tntWars.models.Team;

public interface TeamController {

    void createTeam(String teamName);
    void addPlayerToTeam(String teamName, String playerId);
    void removePlayerFromTeam(String teamName, String playerId);
    Team getTeamByName(String teamName);


}
