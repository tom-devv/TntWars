package dev.tom.tntWars.services;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class WorldManager {

    private static final String TEMPLATE_DIR_NAME = "map_templates"; // This is where the map templates are stored

    private static final List<String> IGNORED_FILES = Arrays.asList("uid.dat", "session.lock");

    public World cloneWorld(World world) throws IOException {
        File source = world.getWorldFolder();
        File target = new File(Bukkit.getWorldContainer(), world.getName() + "_clone");

        cloneWorldFiles(source, target);

        return new WorldCreator(world.getName() + "_clone").createWorld();

    }

    private void cloneWorldFiles(File source, File target) throws IOException {
        if (IGNORED_FILES.contains(source.getName())) {
            return;
        }

        if (source.isDirectory()) {
            if (!target.exists() && !target.mkdirs()) {
                throw new IOException("Couldn't create world directory!");
            }

            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    cloneWorldFiles(new File(source, file), new File(target, file));
                }
            }
        } else {
            try (InputStream in = Files.newInputStream(source.toPath());
                 OutputStream out = Files.newOutputStream(target.toPath())) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }


}
