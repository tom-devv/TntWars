package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TNTWars;

import java.util.HashMap;
import java.util.Map;

public abstract class Controller<U, T> {

    /**
     * Map where the values should be instances of
     * the model which the controller manages
     */
    protected final Map<U, T> instances;

    private final TNTWars plugin;

    public Controller(TNTWars plugin) {
        this.plugin = plugin;
        instances = new HashMap<>();
    }


    public Map<U, T> getInstances() {
        return instances;
    }

    public TNTWars getPlugin() {
        return plugin;
    }



}
