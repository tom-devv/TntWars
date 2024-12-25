package dev.tom.tntWars.models.game;

public enum GameState {
    /**
     * The game is not active and should not have any players yet
     */
    INACTIVE,
    /**
     * The game is active and players are playing
     */
    ACTIVE,
    /**
     * The game is paused and players are not playing
     */
    PAUSED,
    /**
     * The game has ended and players are not playing
     */
    ENDED,
    /**
     * The game state is unknown and something bad has happened
     */
    UNKNOWN;

    public boolean isTransitionAllowed(GameState newState) {
        switch (this) {
            case UNKNOWN:
                return false;
            case INACTIVE:
                return newState == ACTIVE;
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
            case INACTIVE:
                return ACTIVE;
            case ACTIVE:
                return PAUSED;
            case PAUSED:
                return ENDED;
            case ENDED:
                return ENDED;
            default:
                return UNKNOWN;
        }
    }

    public GameState getPreviousState() {
        switch (this) {
            case INACTIVE, ACTIVE:
                return INACTIVE;
            case PAUSED:
                return ACTIVE;
            case ENDED:
                return PAUSED;
            default:
                return UNKNOWN;
        }
    }
}