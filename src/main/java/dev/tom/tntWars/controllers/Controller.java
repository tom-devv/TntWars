package dev.tom.tntWars.controllers;

import dev.tom.tntWars.TntWarsPlugin;

import java.util.HashSet;
import java.util.Set;

public abstract class Controller<T> {

    /**
     *
     */
    protected final Set<T> instances;

    private final TntWarsPlugin plugin;

    public Controller(TntWarsPlugin plugin) {
        this.plugin = plugin;
        instances = new HashSet<>();
    }

    public void addInstance(T instance) {
        instances.add(instance);
    }

    public boolean getInstance(T instance) {
        return instances.contains(instance);
    }

    public void removeInstance(T instance) {
        instances.remove(instance);
    }

    public Set<T> getInstances() {
        return instances;
    }

    public TntWarsPlugin getPlugin() {
        return plugin;
    }



}
