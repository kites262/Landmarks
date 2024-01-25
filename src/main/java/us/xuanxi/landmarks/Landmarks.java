package us.xuanxi.landmarks;

import org.bukkit.plugin.java.JavaPlugin;
import us.xuanxi.landmarks.command.CommandGo;
import us.xuanxi.landmarks.command.CommandLandmark;
import us.xuanxi.landmarks.data.Finals;
import us.xuanxi.landmarks.utils.ConfigReader;

import java.util.Objects;
import java.util.logging.Logger;


public final class Landmarks extends JavaPlugin {
    private final Logger logger = this.getLogger();
    private final ConfigReader cr = new ConfigReader(this);

    @Override
    public void onEnable() {
        log("Landmarks plugin by flew_kites, ready to enable.");
        // Plugin startup logic
        this.saveDefaultConfig();

        //register commands
        Objects.requireNonNull(this.getCommand(Finals.command_prefix))
                .setExecutor(new CommandLandmark(cr));

        Objects.requireNonNull(this.getCommand(Finals.command_go))
                .setExecutor(new CommandGo(cr));

        log("Landmarks plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        log("Saving config to file...");
        // Plugin shutdown logic
        cr.save();
        log("Landmarks plugin has been disabled!");
    }

    public void log(String message) {
        logger.info(message);
    }

    public void logError(String message) {
        logger.severe(message);
    }

    public void logWarning(String message) {
        logger.warning(message);
    }
}
