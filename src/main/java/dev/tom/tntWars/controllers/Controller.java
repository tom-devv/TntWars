package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;

public abstract class Controller {

    private final TntWarsPlugin plugin;

    public Controller(TntWarsPlugin plugin) {
        this.plugin = plugin;
    }

    public TntWarsPlugin getPlugin() {
        return plugin;
    }
}
