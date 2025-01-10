package dev.tom.tntWars.config;

import org.spongepowered.configurate.ConfigurateException;

import java.io.File;
import java.util.List;

public interface ConfigurationLoader<T> {

    /**
     * Load all configurations from a specified directory.
     *
     * @throws ConfigurateException if an error occurs while loading.
     */
    void loadAll() throws ConfigurateException;

    /**
     * Load a single configuration from a file.
     *
     * @param file The file to load the configuration from.
     * @return The loaded configuration object.
     * @throws ConfigurateException if an error occurs while loading.
     */
    T loadConfig(File file) throws ConfigurateException;

    /**
     * Get a configuration by its name.
     *
     * @param name The name of the configuration.
     * @return The configuration object, or null if not found.
     */
    T getConfig(String name);

}
