package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;

import java.util.HashSet;
import java.util.Set;

public abstract class Controller {


    private final TntWarsPlugin plugin;

    public Controller(TntWarsPlugin plugin) {
        this.plugin = plugin;
    }

    public TntWarsPlugin getPlugin() {
        return plugin;
    }



}
