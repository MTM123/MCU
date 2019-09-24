package lv.mtm123.spigotutils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.beans.Transient;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class ConfigManager {

    private static Plugin plugin = null;

    public static void initialize(Plugin plugin) {
        ConfigManager.plugin = plugin;
    }

    @Transient
    public static FileConfiguration load(String filename) {

        if (plugin == null) {
            throw new IllegalStateException("ConfigManager must be initialized!");
        }

        File file = new File(plugin.getDataFolder(), filename);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        try {
            if (!file.exists()) {

                InputStream in = plugin.getResource(filename);

                if (in != null) {
                    plugin.saveResource(filename, false);
                } else {
                    cfg.save(file);
                }

                cfg.load(file);

            }

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return cfg;
    }

    public static void save(FileConfiguration config, String filename) {

        if (plugin == null) {
            throw new IllegalStateException("ConfigManager must be initialized!");
        }

        File file = new File(plugin.getDataFolder(), filename);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void delete(String filename) {

        if (plugin == null) {
            throw new IllegalStateException("ConfigManager must be initialized!");
        }

        File file = new File(plugin.getDataFolder(), filename);

        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean exists(String filename) {

        if (plugin == null) {
            throw new IllegalStateException("ConfigManager must be initialized!");
        }

        File file = new File(plugin.getDataFolder(), filename);

        return file.exists();

    }

}
