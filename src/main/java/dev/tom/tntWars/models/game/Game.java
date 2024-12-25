package dev.tom.tntWars.models.game;

import dev.tom.tntWars.models.map.Map;
import dev.tom.tntWars.models.Team;
import dev.tom.tntWars.utils.NameGenerator;

import java.util.Collection;

public class Game {

    private final String gameId;
    private final GameSettings settings;
    private final Collection<Team> teams;
    private Map map;
    private GameState state;

    public Game(GameSettings settings, Collection<Team> teams){
        this.settings = settings;
        this.teams = teams;
        this.gameId = NameGenerator.generateName();
    }


    public Map getMap() {
        return map;
    }

    public void previousState(){
        GameState newState = state.getPreviousState();
        if (!state.isTransitionAllowed(newState)) {
            throw new IllegalStateException("Transition from " + state + " to " + newState + " is not allowed.");
        }
        state = newState;
    }

    public void nextState() {
        GameState newState = state.getNextState();
        if (!state.isTransitionAllowed(newState)) {
            throw new IllegalStateException("Transition from " + state + " to " + newState + " is not allowed.");
        }
        state = newState;
    }

    public void setMap(Map map) {
        if (this.map != null) {
            throw new IllegalStateException("Map is already set for this game.");
        }
        this.map = map;
    }

    public Collection<Team> getTeams() {
        return teams;
    }

    public GameState getState() {
        return state;
    }

    public GameSettings getSettings() {
        return settings;
    }
}
