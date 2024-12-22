package dev.tom.tntWars.models.game;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.models.Map;
import dev.tom.tntWars.utils.NameGenerator;

import java.util.UUID;

public class Game {

    private final String gameId;
    private final GameSettings settings;
    private Map map;
    private GameState state;

    public Game(GameSettings settings){
        this.settings = settings;
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

    public GameState getState() {
        return state;
    }

    public GameSettings getSettings() {
        return settings;
    }
}
