package dev.tom.tntWars.models.game;

import dev.tom.tntWars.interfaces.MapProvider;
import org.jetbrains.annotations.NotNull;

public class GameSettings {

    private final MapProvider mapProvider;
    private int maxPlayers;
    private int minPlayers;

    public GameSettings(@NotNull  MapProvider mapProvider) {
        this.mapProvider = mapProvider;
    }

    public MapProvider getMapProvider() {
        return mapProvider;
    }
}
