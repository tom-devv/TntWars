package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;
import org.checkerframework.checker.guieffect.qual.UI;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Controller<U, T> {

    /**
     * Map where the values should be instances of
     * the model which the controller manages
     */
    protected final Map<U, T> instances;

    private final TntWarsPlugin plugin;

    public Controller(TntWarsPlugin plugin) {
        this.plugin = plugin;
        instances = new HashMap<>();
    }


    public Map<U, T> getInstances() {
        return instances;
    }

    public TntWarsPlugin getPlugin() {
        return plugin;
    }



}
