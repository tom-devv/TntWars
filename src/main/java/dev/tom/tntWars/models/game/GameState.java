package dev.tom.tntWars.models.game;

public enum GameState {
    ACTIVE,
    PAUSED,
    ENDED;

    public boolean isTransitionAllowed(GameState newState) {
        switch (this) {
            case ACTIVE:
                return newState == PAUSED || newState == ENDED;
            case PAUSED:
                return newState == ACTIVE || newState == ENDED;
            case ENDED:
                return false; // No transitions allowed after the game ends
            default:
                return false;
        }
    }

    public GameState getNextState() {
        switch (this) {
            case ACTIVE:
                return PAUSED;
            case PAUSED:
                return ENDED;
            case ENDED:
                return ENDED;
            default:
                return ENDED;
        }
    }

    public GameState getPreviousState() {
        switch (this) {
            case ACTIVE:
                return ACTIVE;
            case PAUSED:
                return ACTIVE;
            case ENDED:
                return PAUSED;
            default:
                return ACTIVE;
        }
    }
}