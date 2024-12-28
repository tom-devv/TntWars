package dev.tom.tntWars.models.game;

import dev.tom.tntWars.TntWarsPlugin;
import dev.tom.tntWars.interfaces.MapProvider;
import dev.tom.tntWars.services.map.RandomMapProvider;
import dev.tom.tntWars.services.team.TeamProvider;
import org.jetbrains.annotations.NotNull;

public class GameSettings {

    private final MapProvider mapProvider;
    private final TeamProvider teamProvider;
    private int maxPlayers;
    private int minPlayers;

    public GameSettings(@NotNull  MapProvider mapProvider, @NotNull TeamProvider teamProvider) {
        this.mapProvider = mapProvider;
        this.teamProvider = teamProvider;
    }

    public TeamProvider getTeamProvider() {
        return teamProvider;
    }

    public MapProvider getMapProvider() {
        return mapProvider;
    }

    public static GameSettings defaultSettings(){
        return new GameSettings(TntWarsPlugin.getRandomMapProvider(), TntWarsPlugin.getRandomTeamProvider());
    }
}
