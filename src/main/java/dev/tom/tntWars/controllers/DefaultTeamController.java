package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.TeamController;
import dev.tom.tntWars.models.Team;

public class DefaultTeamController extends Controller implements TeamController {

    public DefaultTeamController(TntWarsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void createTeam(String teamName) {

    }

    @Override
    public void addPlayerToTeam(String teamName, String playerId) {

    }

    @Override
    public void removePlayerFromTeam(String teamName, String playerId) {

    }

    @Override
    public Team getTeamByName(String teamName) {
        return null;
    }
}
