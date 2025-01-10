package dev.tom.tntWars.models.game;

import dev.tom.tntWars.TNTWars;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.services.team.TeamProvider;
import org.jetbrains.annotations.NotNull;

public class GameSettings {

    private final MapProvider mapProvider;
    private final TeamProvider teamProvider;
    private int livesPerTeam = 2;
    private int respawnDelaySeconds = 5;
    private int maxPlayers;
    private int minPlayers;
    private int timeSeconds = 60 * 5; // todo config?

    public GameSettings(@NotNull  MapProvider mapProvider, @NotNull TeamProvider teamProvider) {
        this.mapProvider = mapProvider;
        this.teamProvider = teamProvider;
    }

    public int getRespawnDelaySeconds() {
        return respawnDelaySeconds;
    }

    public void setRespawnDelaySeconds(int respawnDelaySeconds) {
        this.respawnDelaySeconds = respawnDelaySeconds;
    }

    public int getLivesPerTeam() {
        return livesPerTeam;
    }

    public TeamProvider getTeamProvider() {
        return teamProvider;
    }

    public MapProvider getMapProvider() {
        return mapProvider;
    }

    public static GameSettings defaultSettings(){
        return new GameSettings(TNTWars.getRandomMapProvider(), TNTWars.getBalancedTeamProvider());
    }

    public int getTimeSeconds() {
        return timeSeconds;
    }
}
