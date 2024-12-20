package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;

public abstract class AbstractController {

    private final TntWarsPlugin plugin;

    public AbstractController(TntWarsPlugin plugin) {
        this.plugin = plugin;
    }

    public TntWarsPlugin getPlugin() {
        return plugin;
    }
}
