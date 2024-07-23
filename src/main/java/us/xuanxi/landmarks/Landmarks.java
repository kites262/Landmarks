package us.xuanxi.landmarks;

import org.bukkit.plugin.java.JavaPlugin;
import us.xuanxi.landmarks.command.CommandGo;
import us.xuanxi.landmarks.command.CommandLandmark;
import us.xuanxi.landmarks.config.Names;
import us.xuanxi.landmarks.service.LandmarkService;

import java.util.Objects;
import java.util.logging.Logger;


public final class Landmarks extends JavaPlugin {
    private final Logger logger = this.getLogger();
    private final LandmarkService lms = new LandmarkService(this);

    @Override
    public void onEnable() {
        log("Landmarks plugin by flew_kites, ready to enable.");
        // Plugin startup logic
        this.saveDefaultConfig();

        //register commands
        Objects.requireNonNull(this.getCommand(Names.command_prefix))
                .setExecutor(new CommandLandmark(lms));

        Objects.requireNonNull(this.getCommand(Names.command_go))
                .setExecutor(new CommandGo(lms));

        log("Landmarks plugin has been enabled!");
    }

    @Override
    public void onDisable() {
        log("Saving config to file...");
        // Plugin shutdown logic
        lms.save();
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
